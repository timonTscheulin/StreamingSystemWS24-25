package tnt.eventstore.event_contract;

public class StoreVehicleRemoved extends BaseStoreEvent{
    private final String vehicleId;

    public StoreVehicleRemoved(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
