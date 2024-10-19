package tnt.cqrs_writer.domain_model.events;

import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.event_contract.StoreVehicleMoved;

public class VehicleMoved implements BaseEvent {
    private final String vehicleId;
    private final AbsolutPosition position;

    public VehicleMoved(String vehicleId, AbsolutPosition position) {
        this.vehicleId = vehicleId;
        this.position = position;
    }

    @Override
    public StoreVehicleMoved toStoreEvent() {
        return new StoreVehicleMoved();
    }
}
