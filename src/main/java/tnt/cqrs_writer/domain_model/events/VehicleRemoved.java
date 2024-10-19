package tnt.cqrs_writer.domain_model.events;

import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.event_contract.BaseStoreEvent;
import tnt.eventstore.event_contract.StoreVehicleRemoved;

public class VehicleRemoved implements BaseEvent {
    private final String vehicleId;

    public VehicleRemoved(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public BaseStoreEvent toStoreEvent() {
        return new StoreVehicleRemoved();
    }
}
