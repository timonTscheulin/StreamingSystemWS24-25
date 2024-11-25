package tnt.eventstore.event_contract.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleNewPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.types.AbsolutPoint;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreVehicleNewPosition extends StoreBaseVehicle {
    private final AbsolutPoint newPosition;

    @JsonCreator
    public StoreVehicleNewPosition(
            @JsonProperty("vehicleId") String vehicleId,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
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
