package tnt.eventstore.event_contract;

import tnt.eventstore.event_contract.types.AbsolutPoint;

public class StoreVehicleCreated extends BaseStoreEvent {
    String vehicleId;
    AbsolutPoint startPosition;

    public StoreVehicleCreated(String vehicleId, int startX, int startY) {
        this.vehicleId = vehicleId;
        startPosition = new AbsolutPoint(startX, startY);
    }

    @Override
    public String getId() {
        return vehicleId;
    }
}
