package tnt.eventstore.event_contract.vehicle;

import tnt.cqrs_writer.domain_model.events.vehicle.VehicleHasVisitedPositionAgain;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

public class StoreVehicleHasVisitedPositionAgain extends StoreVehicleBase {
    public StoreVehicleHasVisitedPositionAgain(String vehicleId) {
        super(vehicleId);
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleHasVisitedPositionAgain(getVehicleId());
    }

    @Override
    public String getEventType() {
        return "StoreVehicleHasVisitedPositionAgain";
    }
}
