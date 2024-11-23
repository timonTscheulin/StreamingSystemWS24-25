package tnt.eventstore.connectors;

import jakarta.jms.JMSException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.List;
import java.util.Properties;


public class KafkaStore implements EventStoreProducer {
    String topic = "kafka_test_topic";
    Properties producerProps = new Properties();

    public KafkaStore() {
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    }

    @Override
    public void storeEvents(List<StoreBaseEvent> events) throws EventStoreException {
        ProducerRecord<String, String> message;
        message = new ProducerRecord<String, String>(topic, "test");
        //Producer<String, String> producer = new KafkaProducer<String, String>(props)
    }
}
