package tnt.cqrs_writer.domain_model.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleCreated;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleNewPosition;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleRemoved;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.EventStore;
import tnt.eventstore.event_contract.vehicle.StoreBaseVehicle;
import tnt.eventstore.event_contract.vehicle.StoreHelperVehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VehicleRepository {
    private static final Logger log = LoggerFactory.getLogger(VehicleRepository.class);

    private final EventStore eventStore;

    public VehicleRepository(EventStore eventStore){
        this.eventStore = eventStore;
    }

    public Vehicle getVehicle(String vehicleId) {
        Vehicle result = new Vehicle(vehicleId);;
        List<DomainBaseEvent> events = new ArrayList<>();

        try {
            events = eventStore.getAllEvents(new StoreHelperVehicle().getEventDomain());
        } catch (Exception e) {
            log.error("Unable to load events.");
            log.error(e.getMessage());
        }

        for (DomainBaseEvent event : events) {
            switch (event) {
                case VehicleCreated createdEvent -> {
                    if (Objects.equals(createdEvent.vehicleId(), vehicleId)) {
                        log.debug("Replay of vehicle {} created", vehicleId);
                        result.replay(createdEvent);
                    }
                }
                case VehicleNewPosition newPositionEvent -> {
                    if (Objects.equals(newPositionEvent.vehicleId(), vehicleId)) {
                        log.debug("Replay of vehicle {} moved", vehicleId);
                        result.replay(newPositionEvent);
                    }
                }
                case VehicleRemoved removedEvent -> {
                    if (Objects.equals(removedEvent.vehicleId(), vehicleId)) {
                        log.debug("Replay of vehicle {} removed", vehicleId);
                        result.replay(removedEvent);
                    }
                }
                case null, default -> {
                    assert event != null;
                    log.warn("Unknown event type: {}", event.getClass().getName());
                }
            }
        }
        return result;
    }
}
