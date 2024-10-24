package tnt.eventstore.event_contract;

import tnt.eventstore.event_contract.types.AbsolutPoint;

public class StoreVehicleNewPosition extends StoreVehicleBase {
    private final AbsolutPoint newPosition;

    public StoreVehicleNewPosition(String vehicleId, int x, int y) {
        super(vehicleId);
        newPosition = new AbsolutPoint(x, y);
    }

}
