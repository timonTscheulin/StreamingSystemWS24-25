package tnt.cqrs_writer.domain_model.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.events.VehicleCreated;
import tnt.cqrs_writer.domain_model.events.VehicleNewPosition;
import tnt.cqrs_writer.domain_model.events.VehicleRemoved;
import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.InMemoryEventStore;

import java.util.List;
import java.util.Objects;


public class VehicleRepository {
    private static final Logger log = LoggerFactory.getLogger(VehicleRepository.class);
    private static VehicleRepository INSTANCE;

    private VehicleRepository(){}

    public static VehicleRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VehicleRepository();
        }
        return INSTANCE;
    }

    public Vehicle getVehicle(String vehicleId) {
        Vehicle result = null;

        List<BaseEvent> events = InMemoryEventStore.getInstance().getEvents();
        for (BaseEvent event : events) {
            if (event instanceof VehicleCreated createdEvent) {
                if(Objects.equals(createdEvent.vehicleId(), vehicleId)) {
                    if (result == null) {
                        result = new Vehicle(vehicleId);
                    }
                    result.replay(createdEvent);
                }
            } else if (event instanceof VehicleNewPosition newPositionEvent) {
                if(Objects.equals(newPositionEvent.vehicleId(), vehicleId) & result != null) {
                    result.replay(newPositionEvent);
                }
            } else if (event instanceof VehicleRemoved removedEvent) {
                if(Objects.equals(removedEvent.vehicleId(), vehicleId)& result != null) {
                    result.replay(removedEvent);
                }
            } else {
                log.warn("Unknown event type: {}", event.getClass().getName());
            }
        }
        return result;
    }
}
