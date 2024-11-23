package tnt.eventstore.connectors;

import kotlin.NotImplementedError;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import java.util.List;
import java.util.Properties;

public class KafkaStore implements EventStoreProducer {
    String topic = "kafka_test_topic";
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")

    @Override
    public void storeEvent(List<DomainBaseEvent> events) throws EventStoreException {
        ProducerRecord<String, String> message;
        message = new ProducerRecord<String, String>(topic, "test");
        Producer<String, String> producer = new KafkaProducer<String, String>(props)
    }

}
