package tnt.eventstore.connectors;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

public class KafkaProducerConnector implements EventStoreProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerConnector.class);
    private Properties producerProperties = new Properties();
    private KafkaProducer<String, byte[]> producer = null;

    public KafkaProducerConnector(String bootstrapServers) {
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        producer = new KafkaProducer<>(producerProperties);
    }

    @Override
    public void storeEvents(List<StoreBaseEvent> events) throws EventStoreException, JMSException {
        if (producer == null) {
            throw new EventStoreException("Kafka Producer is not initialized.");
        }

        for (StoreBaseEvent event : events) {
            String eventType = event.getEventType();
            String eventScope = event.getId();
            String eventDomain = "Test";//event.getEventDomain();
            byte[] payload = null;

            try {
                payload = event.toJson().getBytes(StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("Message serialisation error occurred ... ");
                log.error(e.getMessage(), e);
            }

            ProducerRecord<String, byte[]> record = new ProducerRecord<>(eventDomain, eventScope, payload);

            record.headers().add("event-typ", eventType.getBytes(StandardCharsets.UTF_8));
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Failed to send message: " + exception.getMessage());
                } else {
                    log.info("Message sent to topic " + metadata.topic() + " partition " + metadata.partition());
                }
            });
        }
    }
}
