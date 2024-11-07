package tnt.cqrs_writer.domain_model.events;

import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.event_contract.BaseStoreEvent;
import tnt.eventstore.event_contract.StoreVehicleRemoved;

public record VehicleRemoved(String vehicleId) implements BaseEvent {

    @Override
    public BaseStoreEvent toStoreEvent() {
        return new StoreVehicleRemoved(vehicleId);
    }

    public String vehicleId() {
        return vehicleId;
    }
}
