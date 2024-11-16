package tnt.eventstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.connectors.EventStoreException;
import tnt.eventstore.event_contract.StoreBaseEvent;

import jakarta.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

public class EventStore {
    private final Logger log = LoggerFactory.getLogger(EventStore.class);

    public EventStore() {

    }

    public void store(List<DomainBaseEvent> events) throws EventStoreException, JMSException {
        log.info("Storing events");
    }

    public List<StoreBaseEvent> getAllEvents() throws EventStoreException, JMSException {
        List<StoreBaseEvent> events = new ArrayList<>();
        log.info("Getting all events");
        return events;
    }

    public List<StoreBaseEvent> getAllEventsOfScope(EventScope scope) throws EventStoreException, JMSException {
        List<StoreBaseEvent> events = new ArrayList<>();
        log.info("Getting all events of scope {}", scope);
        return events;
    }

}
