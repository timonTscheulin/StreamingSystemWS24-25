package tnt.eventstore.event_contract;

import tnt.eventstore.event_contract.types.AbsolutPoint;

public class StoreVehicleCreated extends StoreVehicleBase {
    AbsolutPoint startPosition;

    public StoreVehicleCreated(String vehicleId, int startX, int startY) {
        super(vehicleId);
        startPosition = new AbsolutPoint(startX, startY);
    }

    public int getX() {
        return startPosition.x();
    }

    public int getY() {
        return startPosition.y();
    }
}
