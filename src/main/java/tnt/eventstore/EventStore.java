package tnt.eventstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.connectors.EventStoreConnector;
import tnt.eventstore.connectors.EventStoreException;
import tnt.eventstore.event_contract.BaseStoreEvent;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

public class EventStore {
    private static final Logger log = LoggerFactory.getLogger(EventStore.class);
    private static EventStore instance;
    private EventStoreConnector connector;
    private final boolean keepConnectionOpen;
    private final int retryLimit = 3;
    private final int retryDelayMs = 1000;

    private EventStore(EventStoreConnector connector, boolean keepConnectionOpen) {
        this.keepConnectionOpen = keepConnectionOpen;
        this.connector = connector;
        log.info("Creating EventStore instance");
        log.debug("Properties: keepConnectionOpen=" + keepConnectionOpen + ", connector=" + connector.type() );
    }

    public static EventStore getInstance(EventStoreConnector connector, boolean keepConnectionOpen) {
        if (instance == null && connector != null ) {
            instance = new EventStore(connector, keepConnectionOpen);
        }
        else {
            log.warn("EventStore: The creation constructor was called a second time, Use instead getInstance()");
        }
        return instance;
    }

    public static EventStore getInstance() {
        if (instance == null) {
            throw new RuntimeException("You have to add a event store connector at first call");
        }
        return instance;
    }

    /**
     * Speichert mehrere Events auf einmal unter einem bestimmten Scope.
     * @param events Die Liste von Events, die gespeichert werden sollen
     */
    public void store(List<BaseEvent> events) throws EventStoreException, JMSException {
        ensureIsConnected();
        connector.storeEvent(events);
        closeConnection();
    }

    /**
     * Gibt alle gespeicherten Events zurück.
     * @return Liste aller gespeicherten Events
     */
    public List<BaseStoreEvent> getAllEvents() throws EventStoreException, JMSException {
        List<BaseStoreEvent> events = new ArrayList<>();
        ensureIsConnected();
        events = connector.getAllEvents();
        closeConnection();
        return events;
    }

    /**
     * Gibt alle Events eines bestimmten Scopes zurück.
     * @param scope Der Scope, unter dem die Events abgelegt wurden
     * @return Liste der Events, die zum angegebenen Scope gehören
     */
    public List<BaseStoreEvent> getAllEventsOfScope(EventScope scope) throws EventStoreException, JMSException {
        List<BaseStoreEvent> events = new ArrayList<>();
        ensureIsConnected();
        events = connector.fetchEventsByScope(scope);
        closeConnection();
        return events;
    }

    /**
     * Überprüft den verbindungsstatus und versucht die verbindung erneut
     * zu etablieren, wenn es nicht verbunden ist.
     */
    private void ensureIsConnected() throws RuntimeException, EventStoreException {
        isConnectorAvailable();
        if ( !connector.isConnected()) {
            log.info("EventStore: Try reconnecting to storage system...");
            for (int i = 0; i < retryLimit; i++ ) {
                try {
                    connector.connect();
                    log.info("EventStore: Connection to storage system established.");
                    return;
                } catch (EventStoreException | JMSException e) {
                    log.warn("EventStore: Connection attempt " + (i + 1) + " failed.", e);
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
            log.error("EventStore: All connection attempts to storage system failed. Retry limit reached.");
            throw new RuntimeException("Reached maximum retries to establish connection");
        }
        else {
            log.info("EventStore: Still connected to storage system.");
        }
    }

    private void closeConnection() throws EventStoreException, JMSException {
        isConnectorAvailable();
        if (!keepConnectionOpen && connector.isConnected()) {
            connector.disconnect();
            log.info("EventStore: Disconnected from storage subsystem.");
        }
    }

    public void closeFinally() throws EventStoreException, JMSException {
        isConnectorAvailable();
        connector.disconnect();
        connector = null;
        log.info("EventStore: Connection to storage system closed.");
    }

    private void isConnectorAvailable() throws EventStoreException {
        if (connector == null) {
            throw new EventStoreException("Connection is not available. It was closed finally");
        }
    }
}
