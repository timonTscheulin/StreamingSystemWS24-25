package tnt.eventstore.connectors;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.*;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.List;

public class ActiveMQProducerConnector implements EventStoreProducer {

    private static final String defaultBrokerUrl = "tcp://localhost:61616";
    private static final String defaultUsername = "artemis";
    private static final String defaultPassword = "artemis";
    private static final String defaultTopicName = "cqrs_topic";
    private static final Logger log = LoggerFactory.getLogger(ActiveMQProducerConnector.class);

    private final ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;


    public ActiveMQProducerConnector() {
        this.connectionFactory = new ActiveMQConnectionFactory(defaultBrokerUrl);
        try {
            connection = connectionFactory.createConnection(defaultUsername, defaultPassword);
            connection.start();
            this.session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Destination topic = session.createTopic(defaultTopicName);
            this.producer = session.createProducer(topic);
            log.info("ActiveMQ Artemis Store initialized with broker URL {}", defaultBrokerUrl);
        } catch (JMSException e) {
            log.error("Failed to initialize connection: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize ActiveMQ Artemis connection", e);
        }
    }


    @Override
    public void storeEvents(List<StoreBaseEvent> events) throws EventStoreException, JMSException {
        try {
            log.info("Starting to store events in broker {}", defaultBrokerUrl);
            for (StoreBaseEvent event : events) {
                ObjectMessage message = session.createObjectMessage(event);
                producer.send(message);
                log.debug("Event sent: {}", event);
            }
            session.commit();
            log.info("Events successfully committed.");
        } catch (JMSException e) {
            session.rollback();
            log.error("Failed to store events: {}", e.getMessage(), e);
            throw new EventStoreException("Failed to store events", EventStoreException.ErrorCode.STORAGE_ERROR, e);
        }
    }

}