package tnt.eventstore.connectors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.time.Duration;

public class KafkaConsumerConnector implements EventStoreConsumer {


    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConnector.class);
    private final KafkaConsumer<String, String> consumer;

    private static final Map<String, Class<? extends StoreBaseEvent>> EVENT_TYPE_MAP = Map.of(
            "StoreVehicleCreated", tnt.eventstore.event_contract.vehicle.StoreVehicleCreated.class,
            "StoreVehicleRemoved", tnt.eventstore.event_contract.vehicle.StoreVehicleRemoved.class,
            "StoreVehicleNewPosition", tnt.eventstore.event_contract.vehicle.StoreVehicleNewPosition.class,
            "StoreVehicleHasVisitedPositionAgain", tnt.eventstore.event_contract.vehicle.StoreVehicleHasVisitedPositionAgain.class,
            "StorePositionOccupied", tnt.eventstore.event_contract.position.StorePositionOccupied.class,
            "StorePositionReleased", tnt.eventstore.event_contract.position.StorePositionReleased.class,
            "StoreVehicleHasReachedMovementLimit", tnt.eventstore.event_contract.vehicle.StoreVehicleHasReachedMovementLimit.class
    );

    public KafkaConsumerConnector(String bootstrapServers) {

        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Default fallback
        consumerProperties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 20);
        //consumerProperties.put(ConsumerConfig., 500);
        this.consumer = new KafkaConsumer<>(consumerProperties);
    }

    @Override
    public List<StoreBaseEvent> getEventsFromOffset(String eventDomain, Long domainOffset) throws EventStoreException {
        List<StoreBaseEvent> events = new ArrayList<>();
        try {
            // Assign the topic partition manually
            TopicPartition partition = new TopicPartition(eventDomain, 0);
            consumer.assign(Collections.singletonList(partition));

            // Move the consumer to the beginning of the partition
            consumer.seek(partition, domainOffset);
            log.info("Assigned to topic '{}' and seeking to beginning of partition {}", eventDomain, partition);

            // Poll and process records
            ConsumerRecords<String, String> records = null;
            do {
                records = consumer.poll(Duration.ofMillis(100));

                if (records.isEmpty()) {
                    log.info("Finished reading all events from topic '{}'", eventDomain);
                    break;
                }

                records.forEach(record -> {
                    try {
                        byte[] eventTypeHeader = record.headers().lastHeader("event-typ").value();
                        String eventType = new String(eventTypeHeader, StandardCharsets.UTF_8);
                        String payload = record.value();

                        StoreBaseEvent event = StoreBaseEvent.fromJson(payload, resolveEventClass(eventType));
                        events.add(event);

                        log.info("Consumed event: type={} key={} offset={} topic={}",
                                eventType, record.key(), record.offset(), record.topic());
                    } catch (Exception e) {
                        log.error("Failed to deserialize event from Kafka: {}", e.getMessage(), e);
                    }
                });
            } while(!records.isEmpty());
        } catch (Exception e) {
            log.error("Error while consuming events from Kafka: {}", e.getMessage(), e);
            throw new EventStoreException("Error consuming Kafka events", EventStoreException.ErrorCode.RETRIEVAL_ERROR, e);
        }

        return events;
    }

    @Override
    public List<StoreBaseEvent> getAllEvents(String domain) throws EventStoreException {
        return getEventsFromOffset(domain, 0L);
    }


    private Class<? extends StoreBaseEvent> resolveEventClass(String eventType) throws ClassNotFoundException {
        Class<? extends StoreBaseEvent> clazz = EVENT_TYPE_MAP.getOrDefault(eventType, null);

        if (clazz == null) {
            throw new ClassNotFoundException("Unknown event type: " + eventType);
        }

        return clazz;
    }
}
