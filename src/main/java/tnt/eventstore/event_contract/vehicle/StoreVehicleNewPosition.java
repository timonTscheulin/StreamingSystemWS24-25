package tnt.eventstore.event_contract.vehicle;

import tnt.cqrs_writer.domain_model.events.vehicle.VehicleNewPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.types.AbsolutPoint;

public class StoreVehicleNewPosition extends StoreVehicleBase {
    private final AbsolutPoint newPosition;

    public StoreVehicleNewPosition(String vehicleId, int x, int y) {
        super(vehicleId);
        newPosition = new AbsolutPoint(x, y);
    }

    public int getX() {
        return newPosition.x();
    }

    public int getY() {
        return newPosition.y();
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleNewPosition(getVehicleId(), getX(), getY());
    }

    @Override
    public String getEventType() {
        return "StoreVehicleNewPosition";
    }
}
