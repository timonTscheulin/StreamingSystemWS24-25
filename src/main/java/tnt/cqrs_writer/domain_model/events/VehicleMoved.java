package tnt.cqrs_writer.domain_model.events;

import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.event_contract.StoreVehicleMoved;

public class VehicleMoved implements BaseEvent {
    @Override
    public StoreVehicleMoved toStoreEvent() {
        return new StoreVehicleMoved();
    }
}
