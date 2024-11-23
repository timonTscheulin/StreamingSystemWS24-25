package tnt.eventstore.event_contract.vehicle;

import tnt.eventstore.event_contract.StoreBaseEvent;

public abstract class StoreVehicleBase extends StoreBaseEvent {
    private final String vehicleId;

    public StoreVehicleBase(String vehicleId) {
        super();
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getEventScope() {return vehicleId; }

    public String getEventDomain() {
        return "DomainVehicle";
    }

    public String toString() {
        return "StoreVehicleBase [vehicleId=" + vehicleId + "]";
    }
}
