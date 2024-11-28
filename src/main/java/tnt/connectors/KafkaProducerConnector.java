package tnt.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.generators.Events.SensorDataRecord;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.util.List;
import java.util.Properties;

public class KafkaProducerConnector {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerConnector.class);
    private KafkaProducer<String, byte[]> producer = null;
    private ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducerConnector(String bootstrapServers) {
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        producer = new KafkaProducer<>(producerProperties);
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    public void storeEvents(List<SensorDataRecord> events) {
        if (producer == null) {
            throw new RuntimeException("Kafka Producer is not initialized.");
        }

        for (SensorDataRecord event : events) {
            try {
                String topic = "SensorData";

                byte[] payload = objectMapper.writeValueAsBytes(event);

                ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, payload);

                producer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        log.error("Failed to send message: " + exception.getMessage());
                    } else {
                        //log.info("Message sent to topic " + metadata.topic() + " partition " + metadata.partition());
                    }
                });
            } catch (Exception e) {
                 log.error("Failed to serialize messages.");
                 log.error(e.getMessage());
            }
        }
    }
}