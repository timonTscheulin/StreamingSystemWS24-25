package tnt.cqrs_writer.domain_model.events;

import tnt.cqrs_writer.dtypes.PositionPoint;
import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.event_contract.StoreVehicleCreated;

public class VehicleCreated implements BaseEvent {

    private String vehicleId;
    private PositionPoint startPosition;

    public VehicleCreated(String vehicleId, int startX, int startY) {
        this.vehicleId = vehicleId;
        this.startPosition = new PositionPoint(startX, startY);
    }

    @Override
    public StoreVehicleCreated toStoreEvent() {
        return new StoreVehicleCreated(vehicleId, startPosition.x(), startPosition.y());
    }
}
