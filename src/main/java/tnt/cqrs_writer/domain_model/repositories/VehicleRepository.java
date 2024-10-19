package tnt.cqrs_writer.domain_model.repositories;

import tnt.cqrs_writer.domain_model.aggregates.Vehicle;

import java.util.HashMap;
import java.util.Map;


public class VehicleRepository {
    private static VehicleRepository INSTANCE;
    private Map<String, Vehicle> aggregates = new HashMap<>();

    private VehicleRepository(){}

    public static VehicleRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VehicleRepository();
        }
        return INSTANCE;
    }

    public Vehicle getVehicle(String vehicleId) {
        Vehicle result = null;

        if (aggregates.containsKey(vehicleId)) {
            result = aggregates.get(vehicleId);
        }

        return result;
    }

    public void updateVehicle(Vehicle vehicle) {
        /* This is a workaround.
         * In the future we create the vehicle out of events from the event store
         * For the in memory implementation we need this method to commit the update aggregate
         * to the repository.
         */
         if (vehicle.exists()) {
             aggregates.put(vehicle.getVehicleId(), vehicle);
         }
         else {
             aggregates.remove(vehicle.getVehicleId());
         }
    }
}
