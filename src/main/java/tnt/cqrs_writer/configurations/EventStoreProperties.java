package tnt.cqrs_writer.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix="event-store")
public class EventStoreProperties {
    private String type;
    private Kafka kafka = new Kafka();
    private ActiveMQ activemq = new ActiveMQ();

    @Getter
    @Setter
    public static class Kafka {
        // specialised options
        private String bootstrapServers;
    }

    @Getter
    @Setter
    public static class ActiveMQ {
        private String brokerUrl;
        private String topic;
    }
}
