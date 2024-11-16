package tnt.cqrs_writer.domain_model.repositories;

import tnt.cqrs_writer.domain_model.aggregates.Position;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleCreated;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.InMemoryEventStore;

import java.util.List;

public class PositionRepository {
    private static PositionRepository INSTANCE;
    private PositionRepository(){}

    public static PositionRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PositionRepository();
        }
        return INSTANCE;
    }

    public Position getPosition(AbsolutPosition targetPosition) {
        Position position = new Position();

        List<DomainBaseEvent> events = InMemoryEventStore.getInstance().getEvents();
        for (DomainBaseEvent event : events) {
            if (event instanceof VehicleCreated createdEvent) {

            }
        }

        return position;
    }
}
