package tnt.cqrs_writer.domain_model.events.vehicle;

import lombok.Getter;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleNewPosition;

public class VehicleNewPosition implements DomainBaseEvent {
    private final String vehicleId;
    @Getter
    private final AbsolutPosition position;

    public VehicleNewPosition(String vehicleId, int x, int y) {
        this.vehicleId = vehicleId;
        this.position = new AbsolutPosition(x, y);
    }

    @Override
    public StoreVehicleNewPosition toStoreEvent() {
        return new StoreVehicleNewPosition(vehicleId, position.x(), position.y());
    }

    public String vehicleId() {
        return vehicleId;
    }
}
