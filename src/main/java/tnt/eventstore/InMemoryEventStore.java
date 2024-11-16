package tnt.eventstore;

import tnt.cqrs_writer.domain_model.aggregates.Position;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventStore {
    private static InMemoryEventStore INSTANCE = null;
    private List<StoreBaseEvent> events = new ArrayList<>();

    private InMemoryEventStore() {};

    public static InMemoryEventStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryEventStore();
            Position vehiclePositionMap = new Position();
        }
        return INSTANCE;
    }

    public void store(List<DomainBaseEvent> events) {
        for (DomainBaseEvent event : events) {
            StoreBaseEvent message = event.toStoreEvent();
            this.events.add(message);
        }
    }

    public List<DomainBaseEvent> getEvents() {
        List<DomainBaseEvent> baseEvents = new ArrayList<>();
        for (StoreBaseEvent event : events) {
            baseEvents.add(event.toDomainEvent());
        }
        return baseEvents;
    }
}
