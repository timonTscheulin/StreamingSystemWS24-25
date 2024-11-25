package tnt.eventstore.event_contract.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleCreated;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.types.AbsolutPoint;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreVehicleCreated extends StoreVehicleBase {
    private final AbsolutPoint starttPoint;

    @JsonCreator
    public StoreVehicleCreated(
            @JsonProperty("vehicleId") String vehicleId,
            @JsonProperty("startX") int startX,
            @JsonProperty("startY") int startY) {
        super(vehicleId);
        starttPoint = new AbsolutPoint(startX, startY);
    }


    public int getStartX() {
        return starttPoint.x();
    }

    public int getStartY() {
        return starttPoint.y();
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleCreated(getVehicleId(), getStartX(), getStartY());
    }

    @Override
    public String getEventType() {
        return "StoreVehicleCreated";
    }
}
