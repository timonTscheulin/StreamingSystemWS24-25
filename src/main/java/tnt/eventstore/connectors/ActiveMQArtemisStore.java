package tnt.eventstore.connectors;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.BaseStoreEvent;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ActiveMQArtemisStore implements EventStoreConnector {

    private static final String defaultBrokerUrl = "tcp://localhost:61616"; // Broker URL anpassen
    private static final String defaultUsername = "artemis";
    private static final String defaultPassword = "artemis";
    private static final String defaultQueueName = "cqrs_default";
    private static final Logger log = LoggerFactory.getLogger(ActiveMQArtemisStore.class);
    private String username;
    private String password;
    private String brokerUrl;

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;

    public ActiveMQArtemisStore() {
        this(defaultBrokerUrl, defaultUsername, defaultPassword);
    }

    public ActiveMQArtemisStore(String brokerUrl, String username, String password) {
        this.username = username;
        this.password = password;
        this.brokerUrl = brokerUrl;
        connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
    }

    @Override
    public void storeEvent(List<BaseEvent> events) throws EventStoreException, JMSException {
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        try {
            log.info("Start session to store events in broker {}", brokerUrl);
            Queue queue = session.createQueue(defaultQueueName);
            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            for (BaseEvent event : events) {
                ObjectMessage message = session.createObjectMessage(event.toStoreEvent());
                producer.send(message);
            }

            session.commit();
            log.info("Events successfully commited.");
        } catch (Exception e) {
            session.rollback();
            log.error(e.getMessage(), e);
            throw new EventStoreException("Failed to store events", EventStoreException.ErrorCode.STORAGE_ERROR, e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<BaseStoreEvent> fetchEventsByScope(EventScope scope) throws EventStoreException {
        List<BaseStoreEvent> events = this.getAllEvents();
        // possibly a jms selector is a better solution?
        return events.stream()
                .filter(event -> event.getClass().equals(scope.getEventType()) && event.getId().equals(scope.getId()))
                .toList();
    }

    @Override
    public List<BaseStoreEvent> getAllEvents() throws EventStoreException {
        List<BaseStoreEvent> events = new ArrayList<>();
        // do we need a second method which allows projectors to pull only the newest events?
        // better performance ...
        Session session = null;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(defaultQueueName);
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration<?> messages = browser.getEnumeration();

            while (messages.hasMoreElements()) {
                Message message = (Message) messages.nextElement();
                if (message instanceof ObjectMessage objectMessage) {
                    events.add((BaseStoreEvent) objectMessage.getObject());
                }
            }

        } catch (JMSException e) {
            log.error(e.getMessage(), e);
            throw new EventStoreException("Failed to retrieve events", EventStoreException.ErrorCode.CONNECTION_ERROR, e);
        } finally {
            try {
                log.info("Closing read session.");
                if (session != null) {
                    session.close();
                }
            } catch (JMSException e) {
                log.warn(e.getMessage(), e);
            }
        }
        return events;
    }

    @Override
    public void connect() throws JMSException {
        if (connection == null) {
            connection = connectionFactory.createConnection(this.username, this.password);
        }
    }

    @Override
    public void disconnect() throws JMSException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public String type() {
        return "ActiveMq Artemis Connector";
    }
}
