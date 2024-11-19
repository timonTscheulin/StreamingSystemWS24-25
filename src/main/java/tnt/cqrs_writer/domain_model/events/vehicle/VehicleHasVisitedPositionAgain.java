package tnt.cqrs_writer.domain_model.events.vehicle;

import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleHasVisitedPositionAgain;

public class VehicleHasVisitedPositionAgain implements DomainBaseEvent {
    private String vehicleId;

    public VehicleHasVisitedPositionAgain(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public StoreBaseEvent toStoreEvent() {
        return new StoreVehicleHasVisitedPositionAgain(vehicleId);
    }
}
