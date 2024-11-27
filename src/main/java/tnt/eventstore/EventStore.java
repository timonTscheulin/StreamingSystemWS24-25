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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class EventStore {
    private final Logger log = LoggerFactory.getLogger(EventStore.class);
    private List<EventStoreProducer> producers;
    private EventStoreConsumer consumer;
    private final Map<String, Long> domainOffsets = new HashMap<>();

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

    public List<DomainBaseEvent> getAllEvents(String eventDomain) throws EventStoreException {
        log.info("Getting all events");
        List<DomainBaseEvent> events = convertToDomainEvents(consumer.getAllEvents(eventDomain));
        log.info("read of events done");
        return events;
    }

    public List<StoreBaseEvent> getLatestEvents(String eventDomain) throws EventStoreException {
        long domainOffset = domainOffsets.getOrDefault(eventDomain, 0L);
        List<StoreBaseEvent> events = consumer.getEventsFromOffset(eventDomain, domainOffset);
        log.info("Getting latest events: event count:  {}", events.size());
        domainOffsets.put(eventDomain, domainOffset + events.size());
        return events;
    }

    private List<DomainBaseEvent>convertToDomainEvents(List<StoreBaseEvent> storeBaseEvents) {
        return storeBaseEvents.stream()
                .map(StoreBaseEvent::toDomainEvent)
                .collect(Collectors.toList());
    }
}
