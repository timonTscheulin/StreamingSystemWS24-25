package tnt.cqrs_writer.domain_model.events;

import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.event_contract.StoreVehicleCreated;

public class VehicleCreated implements BaseEvent {
    @Override
    public StoreVehicleCreated toStoreEvent() {
        return new StoreVehicleCreated();
    }
}
