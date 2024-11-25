package tnt.cqrs_writer.domain_model.events.vehicle;

import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleHasReachedMovementLimit;

public class VehicleHasReachedMovementLimit implements DomainBaseEvent {
    private final String vehicleId;

    public VehicleHasReachedMovementLimit(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public StoreBaseEvent toStoreEvent() {
        return new StoreVehicleHasReachedMovementLimit(vehicleId);
    }
}
