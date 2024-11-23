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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.time.Duration;

public class KafkaConsumerConnector implements EventStoreConsumer {


    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConnector.class);
    private final KafkaConsumer<String, String> consumer;

    public KafkaConsumerConnector(String bootstrapServers) {

        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Default fallback
        this.consumer = new KafkaConsumer<>(consumerProperties);
    }

    @Override
    public List<StoreBaseEvent> getAllEvents() throws EventStoreException {
        String eventDomain = "Test";
        List<StoreBaseEvent> events = new ArrayList<>();
        try {
            // Assign the topic partition manually
            TopicPartition partition = new TopicPartition(eventDomain, 0);
            consumer.assign(Collections.singletonList(partition));

            // Move the consumer to the beginning of the partition
            consumer.seekToBeginning(Collections.singletonList(partition));
            log.info("Assigned to topic '{}' and seeking to beginning of partition {}", eventDomain, partition);

            // Poll and process records
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

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
            }
        } catch (Exception e) {
            log.error("Error while consuming events from Kafka: {}", e.getMessage(), e);
            throw new EventStoreException("Error consuming Kafka events", EventStoreException.ErrorCode.RETRIEVAL_ERROR, e);
        }

        return events;
    }

    private Class<? extends StoreBaseEvent> resolveEventClass(String eventType) throws ClassNotFoundException {
        // Implement a mapping or reflection-based resolution of event types to classes
        switch (eventType) {
            case "StoreVehicleCreated":
                return tnt.eventstore.event_contract.vehicle.StoreVehicleCreated.class;
            case "StoreVehicleRemoved":
                return tnt.eventstore.event_contract.vehicle.StoreVehicleRemoved.class;
            case "StoreVehicleNewPosition":
                return tnt.eventstore.event_contract.vehicle.StoreVehicleNewPosition.class;
            case "StoreVehicleHasVisitedPositionAgain":
                return tnt.eventstore.event_contract.vehicle.StoreVehicleHasVisitedPositionAgain.class;
            case "StorePositionOccupied":
                return tnt.eventstore.event_contract.position.StorePositionOccupied.class;
            case "StorePositionReleased":
                return tnt.eventstore.event_contract.position.StorePositionReleased.class;
            default:
                throw new ClassNotFoundException("Unknown event type: " + eventType);
        }
    }
}
