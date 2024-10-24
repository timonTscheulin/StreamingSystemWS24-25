package tnt.eventstore.event_contract;

public class StoreVehicleBase extends BaseStoreEvent {
    private final String vehicleId;

    public StoreVehicleBase(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String getId() {
        return vehicleId;
    }

    public String toString() {
        return "StoreVehicleBase [vehicleId=" + vehicleId + "]";
    }
}
