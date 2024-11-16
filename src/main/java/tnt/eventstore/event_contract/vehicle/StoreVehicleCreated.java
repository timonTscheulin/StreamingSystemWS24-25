package tnt.eventstore.event_contract.vehicle;

import tnt.cqrs_writer.domain_model.events.vehicle.VehicleCreated;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
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

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleCreated(getVehicleId(), getX(), getY());
    }
}
