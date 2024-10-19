package tnt.eventstore.event_contract;

import tnt.eventstore.event_contract.types.MoveVector;

public class StoreVehicleMoved extends BaseStoreEvent {
    String vehicleId;
    MoveVector positionDelta;

}
