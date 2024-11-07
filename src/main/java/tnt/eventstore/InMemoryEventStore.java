package tnt.eventstore;

import tnt.cqrs_writer.domain_model.aggregates.VehiclePositionMap;
import tnt.cqrs_writer.framework.events.BaseEvent;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventStore {
    private static InMemoryEventStore INSTANCE = null;
    private List<BaseEvent> events = new ArrayList<>();

    private InMemoryEventStore() {};

    public static InMemoryEventStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryEventStore();
            VehiclePositionMap vehiclePositionMap = new VehiclePositionMap();
        }
        return INSTANCE;
    }

    public void store(List<BaseEvent> events) {
        this.events.addAll(events);
    }

    public List<BaseEvent> getEvents() {
        return events;
    }
}
