package tnt.eventstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.connectors.EventStoreConsumer;
import tnt.eventstore.connectors.EventStoreException;
import tnt.eventstore.connectors.EventStoreProducer;
import tnt.eventstore.event_contract.StoreBaseEvent;

import jakarta.jms.JMSException;
import java.util.ArrayList;
import java.util.List;


public class EventStore {
    private final Logger log = LoggerFactory.getLogger(EventStore.class);
    private List<EventStoreProducer> producers;
    private EventStoreConsumer consumer;

    public EventStore(List<EventStoreProducer> producers, EventStoreConsumer consumer) {
        this.producers = producers;
        this.consumer = consumer;
    }

    public void store(List<DomainBaseEvent> events) throws EventStoreException, JMSException {
        log.info("Storing events");

        List<StoreBaseEvent> storeBaseEvents = new ArrayList<>();
        for (DomainBaseEvent event : events) {
            storeBaseEvents.add(event.toStoreEvent());
        }

        for (EventStoreProducer producer: producers) {
            producer.storeEvents(storeBaseEvents);
        }
        log.info("Store events successful");
    }

    public List<StoreBaseEvent> getAllEvents() throws EventStoreException, JMSException {
        List<StoreBaseEvent> events = new ArrayList<>();
        log.info("Getting all events");
        return consumer.getAllEvents();
    }

    public List<StoreBaseEvent> getLatestEvents() throws EventStoreException, JMSException {
        return null;
    }
}
