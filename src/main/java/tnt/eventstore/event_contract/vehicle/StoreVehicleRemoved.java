package tnt.eventstore.event_contract.vehicle;

import tnt.cqrs_writer.domain_model.events.vehicle.VehicleRemoved;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

public class StoreVehicleRemoved extends StoreVehicleBase{

    public StoreVehicleRemoved(String vehicleId) {
        super(vehicleId);
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new VehicleRemoved(getVehicleId());
    }
}
