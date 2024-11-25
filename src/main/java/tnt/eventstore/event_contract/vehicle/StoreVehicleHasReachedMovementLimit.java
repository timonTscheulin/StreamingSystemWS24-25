package tnt.eventstore.event_contract.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleHasReachedMovementLimit;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreVehicleHasReachedMovementLimit extends StoreBaseVehicle {
    @JsonCreator
    public StoreVehicleHasReachedMovementLimit(
            @JsonProperty("vehicleId") String vehicleId) {
        super(vehicleId);
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleHasReachedMovementLimit(getVehicleId());
    }

    @Override
    public String getEventType() {
        return "StoreVehicleHasReachedMovementLimit";
    }
}
