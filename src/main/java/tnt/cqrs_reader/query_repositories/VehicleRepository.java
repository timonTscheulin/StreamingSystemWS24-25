package tnt.cqrs_reader.query_repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_reader.dtos.SimpleVehicleDTO;
import tnt.cqrs_reader.dtos.VehicleDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleRepository implements BaseRepositories {
    private static final Logger log = LoggerFactory.getLogger(VehicleRepository.class);
    volatile Map<String, VehicleDTO> availableVehicles = new HashMap<>();

    public synchronized void createVehicle(String vehicleId, int x, int y) {
        log.debug("Creating vehicle {} with x={} y={}", vehicleId, x, y);
        VehicleDTO vehicle = new SimpleVehicleDTO(vehicleId, x, y);
        availableVehicles.put(vehicleId, vehicle);
    }

    public synchronized void deleteVehicle(String vehicleId) {
        log.debug("Deleting vehicle {}", vehicleId);
        availableVehicles.remove(vehicleId);
    }

    public synchronized VehicleDTO getVehicle(String vehicleId) {
        log.debug("Retrieving vehicle {}", vehicleId);
        return availableVehicles.get(vehicleId);
    }

    public synchronized List<VehicleDTO> getAllVehicles() {
        log.debug("Retrieving all vehicles");
        return new ArrayList<>(availableVehicles.values());
    }
}
