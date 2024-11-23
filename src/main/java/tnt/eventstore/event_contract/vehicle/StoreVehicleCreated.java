package tnt.eventstore.event_contract.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleCreated;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.types.AbsolutPoint;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreVehicleCreated extends StoreVehicleBase {
    private final int startX;
    private final int startY;

    @JsonCreator
    public StoreVehicleCreated(
            @JsonProperty("vehicleId") String vehicleId,
            @JsonProperty("startX") int startX,
            @JsonProperty("startY") int startY) {
        super(vehicleId);
        this.startX = startX;
        this.startY = startY;
    }


    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
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
