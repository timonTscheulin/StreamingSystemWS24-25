package tnt.cqrs_reader.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tnt.eventstore.EventStore;
import tnt.eventstore.connectors.KafkaConsumerConnector;

import java.util.List;

@Configuration
public class EventStoreConfiguration {

    @Bean(name = "vehicleEventStore")
    public EventStore vehicleEventStore() {
        KafkaConsumerConnector vehicleConsumerConnector = new KafkaConsumerConnector("localhost:29092");
        return new EventStore(List.of(), vehicleConsumerConnector);
    }

    @Bean(name = "vehiclePositionEventStore")
    public EventStore vehiclePositionEventStore() {
        KafkaConsumerConnector vehiclePositionConsumerConnector = new KafkaConsumerConnector("localhost:29092");
        return new EventStore(List.of(), vehiclePositionConsumerConnector);
    }
}