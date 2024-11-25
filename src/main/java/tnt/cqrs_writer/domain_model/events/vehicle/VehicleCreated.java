package tnt.cqrs_writer.domain_model.events.vehicle;

import lombok.Getter;
import tnt.cqrs_writer.dtypes.PositionPoint;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleCreated;

public class VehicleCreated implements DomainBaseEvent {

    private final String vehicleId;
    @Getter
    private PositionPoint startPosition;

    public VehicleCreated(String vehicleId, int startX, int startY) {
        this.vehicleId = vehicleId;
        this.startPosition = new PositionPoint(startX, startY);
    }

    @Override
    public StoreVehicleCreated toStoreEvent() {
        return new StoreVehicleCreated(vehicleId, startPosition.x(), startPosition.y());
    }

    public String vehicleId() {
        return vehicleId;
    }
}
