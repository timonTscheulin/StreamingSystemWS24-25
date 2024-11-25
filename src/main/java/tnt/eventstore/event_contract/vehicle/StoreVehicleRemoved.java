package tnt.eventstore.event_contract.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleRemoved;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreVehicleRemoved extends StoreVehicleBase{

    @JsonCreator
    public StoreVehicleRemoved(
            @JsonProperty("vehicleId") String vehicleId
    ) {
        super(vehicleId);
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleRemoved(getVehicleId());
    }

    @Override
    public String getEventType() {
        return "StoreVehicleRemoved";
    }
}
