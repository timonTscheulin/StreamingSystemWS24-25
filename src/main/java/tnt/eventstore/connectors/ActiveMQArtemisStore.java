package tnt.eventstore.connectors;

import kotlin.NotImplementedError;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.BaseStoreEvent;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class ActiveMQArtemisStore implements EventStoreConnector {

    private static final String defaultBrokerUrl = "tcp://localhost:61616"; // Broker URL anpassen
    private static final String defaultUsername = "artemis";
    private static final String defaultPassword = "artemis";
    private static final String defaultQueueName = "default";
    private String username;
    private String password;
    private String brokerUrl;

    private ActiveMQConnectionFactory connectionFactory;
    private JMSContext connectionContext;

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
    public void storeEvent(BaseStoreEvent event, EventScope scope) throws EventStoreException {
        JMSProducer producer = connectionContext.createProducer();
        producer.send(connectionContext.createQueue(defaultQueueName), event);
    }

    @Override
    public List<BaseStoreEvent> fetchEventsByScope(EventScope scope) {
        /* only placeholder for later feature */
        throw new NotImplementedError();
    }

    @Override
    public List<BaseStoreEvent> getAllEvents() throws EventStoreException {
        List<BaseStoreEvent> events = new ArrayList<>();
        Queue queue = connectionContext.createQueue(defaultQueueName);
        JMSConsumer consumer = connectionContext.createConsumer(queue);

        try {
            Message message;
            while ((message = consumer.receive(1000)) != null) { // Timeout of 1 second
                if (message instanceof ObjectMessage) {
                    events.add((BaseStoreEvent) ((ObjectMessage) message).getObject());
                }
            }
        } catch (JMSException e) {
            throw new EventStoreException("Failed to retrieve all events", EventStoreException.ErrorCode.RETRIEVAL_ERROR, e);
        }

        return events;
    }

    @Override
    public void connect() {
        if (connectionContext == null) {
            connectionContext = connectionFactory.createContext(this.username, this.password);
        }
    }

    @Override
    public void disconnect() {
        if (connectionContext != null) {
            connectionContext.close();
            connectionContext = null;
        }
    }

    @Override
    public boolean isConnected() {
        return connectionContext != null;
    }

    @Override
    public String type() {
        return "ActiveMq Artemis Connector";
    }
}
