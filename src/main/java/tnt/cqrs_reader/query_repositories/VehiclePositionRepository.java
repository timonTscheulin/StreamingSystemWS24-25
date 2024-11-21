package tnt.cqrs_reader.query_repositories;

import tnt.cqrs_reader.dtos.SimpleVehicleDTO;
import tnt.cqrs_reader.dtos.VehicleDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiclePositionRepository implements BaseRepositories {
    private Map<String, VehicleDTO> vehiclesByName = new HashMap<>();
    private Map<String, List<VehicleDTO>> vehiclesByPosition = new HashMap<>();

    public synchronized void add(String vehicleId, int x, int y) {
        if ( !vehiclesByName.containsKey(vehicleId) ) {
            VehicleDTO vehicle = new SimpleVehicleDTO(vehicleId, x, y,0);
            vehiclesByName.put(vehicleId, vehicle);

            if ( !vehiclesByPosition.containsKey(x + "_" + y)) {
                vehiclesByPosition.put(x + "_" + y, new ArrayList<>());
            }

            vehiclesByPosition.get(x + "_" + y).add(vehicle);
        }
    }

    public synchronized void remove(String vehicleId) {
         VehicleDTO vehicle = vehiclesByName.get(vehicleId);

         vehiclesByName.remove(vehicleId);
         String vehicleKey = vehicle.getPosition().x() + "_" + vehicle.getPosition().y();
         List<VehicleDTO> vehicles = vehiclesByPosition.get(vehicleKey);

         if (vehicles.size() <= 1) {
             vehiclesByPosition.remove(vehicleKey);
         } else {
             vehicles.remove(vehicle);
         }
    }

    public synchronized void move(String name, int toX, int toY) {
         remove(name);
         add(name, toX, toY);
    }

    public synchronized List<VehicleDTO> getByVehiclePosition(int x, int y) {
        return vehiclesByPosition.get( x + "_" + y);
    }
}
