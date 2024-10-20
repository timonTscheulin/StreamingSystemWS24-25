package tnt.cqrs_writer.domain_model.events;

import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.event_contract.StoreVehicleNewPosition;

public class VehicleNewPosition implements BaseEvent {
    private final String vehicleId;
    private final AbsolutPosition position;

    public VehicleNewPosition(String vehicleId, int x, int y) {
        this.vehicleId = vehicleId;
        this.position = new AbsolutPosition(x, y);
    }

    @Override
    public StoreVehicleNewPosition toStoreEvent() {
        return new StoreVehicleNewPosition(vehicleId, position.x(), position.y());
    }
}
