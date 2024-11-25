package tnt.cqrs_writer.domain_model.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleCreated;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleNewPosition;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleRemoved;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.EventStore;
import tnt.eventstore.connectors.InMemoryEventStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class VehicleRepository {
    private static final Logger log = LoggerFactory.getLogger(VehicleRepository.class);

    private EventStore eventStore;

    public VehicleRepository(EventStore eventStore){
        this.eventStore = eventStore;
    }

    public Vehicle getVehicle(String vehicleId) {
        Vehicle result = new Vehicle(vehicleId);;
        List<DomainBaseEvent> events = new ArrayList<>();

        try {
            events = eventStore.getAllEvents();
        } catch (Exception e) {
            log.error("Unable to load events.");
            log.error(e.getMessage());
        }

        for (DomainBaseEvent event : events) {
            if (event instanceof VehicleCreated createdEvent) {
                if(Objects.equals(createdEvent.vehicleId(), vehicleId)) {
                    log.debug("Replay of vehicle {} created", vehicleId);
                    // if (result == null) {
                    //    result = new Vehicle(vehicleId);
                    //}
                    result.replay(createdEvent);
                }
            } else if (event instanceof VehicleNewPosition newPositionEvent) {
                if(Objects.equals(newPositionEvent.vehicleId(), vehicleId) & result != null) {
                    log.debug("Replay of vehicle {} moved", vehicleId);
                    result.replay(newPositionEvent);
                }
            } else if (event instanceof VehicleRemoved removedEvent) {
                if(Objects.equals(removedEvent.vehicleId(), vehicleId)& result != null) {
                    log.debug("Replay of vehicle {} removed", vehicleId);
                    result.replay(removedEvent);
                }
            } else {
                log.warn("Unknown event type: {}", event.getClass().getName());
            }
        }
        return result;
    }
}
