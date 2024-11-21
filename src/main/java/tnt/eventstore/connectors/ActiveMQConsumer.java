package tnt.eventstore.connectors;


import jakarta.jms.*;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.ArrayList;
import java.util.List;

public class ActiveMQConsumer implements EventStoreConsumer {

    private static final String defaultBrokerUrl = "tcp://localhost:61616";
    private static final String defaultUsername = "artemis";
    private static final String defaultPassword = "artemis";
    private static final String defaultTopicName = "cqrs_topic";
    private static final Logger log = LoggerFactory.getLogger(ActiveMQConsumer.class);

    private final ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public ActiveMQConsumer() {
        this.connectionFactory = new ActiveMQConnectionFactory(defaultBrokerUrl);
        try {
            connection = connectionFactory.createConnection(defaultUsername, defaultPassword);
            connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination topic = session.createTopic(defaultTopicName);
            this.consumer = session.createConsumer(topic);
            log.info("ActiveMQ Artemis Store initialized with broker URL {}", defaultBrokerUrl);
        } catch (JMSException e) {
            log.error("Failed to initialize connection: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize ActiveMQ Artemis connection", e);
        }
    }

    @Override
    public List<StoreBaseEvent> getAllEvents() throws EventStoreException {
        List<StoreBaseEvent> events = new ArrayList<>();
        try {
            log.debug("Attempting to read events from broker {}", defaultBrokerUrl);
            Message message;
            do {
                message = consumer.receive(5000); // 5 Sekunden Timeout
                if (message instanceof ObjectMessage objectMessage) {
                    events.add((StoreBaseEvent) objectMessage.getObject());
                    log.debug("Received event: {}", objectMessage);
                }
            } while (message != null);
            log.debug("Number of events received: {}", events.size());
        } catch (JMSException e) {
            log.error("Failed to retrieve events: {}", e.getMessage(), e);
            throw new EventStoreException("Failed to retrieve events", EventStoreException.ErrorCode.CONNECTION_ERROR, e);
        }
        return events;
    }
}
