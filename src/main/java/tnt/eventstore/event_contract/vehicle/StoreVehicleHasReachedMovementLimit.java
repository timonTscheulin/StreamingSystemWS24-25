package tnt.eventstore.event_contract.vehicle;

import tnt.cqrs_writer.domain_model.events.vehicle.VehicleHasReachedMovementLimit;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

public class StoreVehicleHasReachedMovementLimit extends StoreVehicleBase {
    public StoreVehicleHasReachedMovementLimit(String vehicleId) {
        super(vehicleId);
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleHasReachedMovementLimit(getVehicleId());
    }
}
