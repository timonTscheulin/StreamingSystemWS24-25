package tnt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_reader.service.QueryInstance;
import tnt.cqrs_reader.dtos.VehicleDTO;
import tnt.cqrs_reader.dtypes.Position;
import tnt.cqrs_writer.api.SimpleCommandsApi;
import tnt.cqrs_writer.dtypes.PositionDelta;
import tnt.cqrs_writer.dtypes.PositionPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            // Setup the query instance and projector start
            QueryInstance queryInstance = new QueryInstance();
            queryInstance.startProjectors();

            Thread.sleep(1000); // Wait for the projector to start
            SimpleCommandsApi api = new SimpleCommandsApi();

            // Initial Test Data
            api.createVehicle("Vehicle1", new PositionPoint(0, 0));
            api.createVehicle("Vehicle2", new PositionPoint(5, 5));
            api.createVehicle("Vehicle3", new PositionPoint(-2, -2));

            // Move Commands
            api.moveVehicle("Vehicle1", new PositionDelta(2, 0)); // Expected Position: (2, 0)
            api.moveVehicle("Vehicle2", new PositionDelta(0, 3)); // Expected Position: (5, 8)
            api.moveVehicle("Vehicle3", new PositionDelta(-1, -1)); // Expected Position: (-3, -3)

            // Expected Positions Map
            Map<String, PositionPoint> expectedPositions = new HashMap<>();
            expectedPositions.put("Vehicle1", new PositionPoint(2, 0));
            expectedPositions.put("Vehicle2", new PositionPoint(5, 8));
            expectedPositions.put("Vehicle3", new PositionPoint(-3, -3));

            // Observation Loop with Validation
            for (int i = 0; i < 5; i++) {
                log.info("Observing vehicles' positions...");

                List<VehicleDTO> vehicles = queryInstance.getVehicles();
                for (VehicleDTO vehicleDTO : vehicles) {
                    String name = vehicleDTO.getName();
                    PositionPoint expectedPosition = expectedPositions.get(name);
                    Position actualPosition = vehicleDTO.getPosition();

                    // Validate if actual position matches expected position
                    if (!Objects.equals(expectedPosition, actualPosition)) {
                        log.error("Position mismatch for " + name +
                                ". Expected: " + expectedPosition +
                                ", but found: " + actualPosition);
                    } else {
                        log.info("Position validation passed for " + name + ": " + actualPosition);
                    }
                }
                Thread.sleep(1000); // Pause before the next iteration
            }

            // Vehicle Removal Test
            log.info("Removing Vehicle1...");
            api.removeVehicle("Vehicle1");
            Thread.sleep(500); // Allow time for removal to propagate

            // Validate Vehicle1 has been removed
            VehicleDTO removedVehicle = queryInstance.getVehicleByName("Vehicle1");
            if (removedVehicle != null) {
                log.error("Vehicle1 should have been removed, but it still exists: " + removedVehicle);
            } else {
                log.info("Vehicle1 removal validated successfully.");
            }

        } catch (Exception e) {
            log.error("Error in TestClient execution: " + e.getMessage(), e);
        }
    }
}
