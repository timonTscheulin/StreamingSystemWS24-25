package tnt;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaMain {
    private static String topic = "test-topic";

    public static void main(String[] args) {

        // Kafka-Producer-Konfiguration
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Producer erstellen
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        // Nachricht an Kafka-Topic senden
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", "Hello, Kafka!");
        producer.send(record);
        System.out.println("Nachricht gesendet: " + record.value());

        // Producer schließen
        producer.close();

        // Kafka-Consumer-Konfiguration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  // Hol alle Nachrichten ab dem Anfang

        // Consumer erstellen
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        // Topic abonnieren
        consumer.subscribe(Collections.singletonList(topic));

        // Nachrichten abrufen
        ConsumerRecords<String, String> records = consumer.poll(5000);  // Timeout nach 5 Sekunden
        for (ConsumerRecord<String, String> rec : records) {
            System.out.printf("Abgeholte Nachricht: key = %s, value = %s%n", rec.key(), rec.value());
        }

        // Consumer schließen
        consumer.close();
    }
}
