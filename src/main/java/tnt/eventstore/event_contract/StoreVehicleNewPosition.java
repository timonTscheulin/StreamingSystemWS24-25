package tnt.eventstore.event_contract;

import tnt.eventstore.event_contract.types.AbsolutPoint;

public class StoreVehicleNewPosition extends BaseStoreEvent {
    private final String vehicleId;
    private final AbsolutPoint newPosition;

    public StoreVehicleNewPosition(String vehicleId, int x, int y) {
        this.vehicleId = vehicleId;
        newPosition = new AbsolutPoint(x, y);
    }

    @Override
    public String getId() {
        return vehicleId;
    }
}
