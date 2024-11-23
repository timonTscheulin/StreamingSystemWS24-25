package tnt.eventstore;

import kotlin.NotImplementedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tnt.eventstore.connectors.EventStoreConsumer;
import tnt.eventstore.connectors.EventStoreProducer;
import tnt.eventstore.connectors.KafkaConsumerConnector;
import tnt.eventstore.connectors.KafkaProducerConnector;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(EventStoreProperties.class)
public class EventStoreConfiguration {
    private static final Logger log = LoggerFactory.getLogger(EventStoreConfiguration.class);
    private final EventStoreProperties properties;

    public EventStoreConfiguration(EventStoreProperties properties) {
        this.properties = properties;
    }

    @Bean
    public EventStore createEventStore() {
        String type = properties.getType();

        if ("Kafka".equalsIgnoreCase(type)) {
            // Use multiple producers to allow to write into multiple message systems
            List<EventStoreProducer> producers = new ArrayList<>();
            // It makes only sense to read from one of the message systems
            EventStoreConsumer consumer = null;

            producers.add(new KafkaProducerConnector(properties.getKafka().getBootstrapServers()));
            consumer = new KafkaConsumerConnector(properties.getKafka().getBootstrapServers());

            return new EventStore(producers, consumer);
        } else if ("ActiveMQ".equalsIgnoreCase(type)) {
            throw new NotImplementedError("ActiveMq Artemis Event Store not implemented");
        } else if ("InMemory".equalsIgnoreCase(type)) {
            throw new NotImplementedError("In Memory EventStore is not yet supported");
        } else {
            log.error("Invalid event store type: {}", type);
            throw new RuntimeException("Invalid event store type: " + type);
        }
    }
}
