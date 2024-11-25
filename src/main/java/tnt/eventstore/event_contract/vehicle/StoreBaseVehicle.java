package tnt.eventstore.event_contract.vehicle;

import tnt.eventstore.event_contract.StoreBaseEvent;

public abstract class StoreBaseVehicle extends StoreBaseEvent {
    private final String vehicleId;

    public StoreBaseVehicle(String vehicleId) {
        super();
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getEventScope() {return vehicleId; }

    public String getEventDomain() {
        return "Vehicle";
    }

    public String toString() {
        return "StoreVehicleBase [vehicleId=" + vehicleId + "]";
    }
}
