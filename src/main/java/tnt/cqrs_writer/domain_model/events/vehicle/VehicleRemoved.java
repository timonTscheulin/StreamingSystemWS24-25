package tnt.cqrs_writer.domain_model.events.vehicle;

import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleRemoved;

public record VehicleRemoved(String vehicleId) implements DomainBaseEvent {

    @Override
    public StoreBaseEvent toStoreEvent() {
        return new StoreVehicleRemoved(vehicleId);
    }

    public String vehicleId() {
        return vehicleId;
    }
}
