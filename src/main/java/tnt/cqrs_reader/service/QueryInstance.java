package tnt.cqrs_reader.service;

import tnt.cqrs_reader.api.VehicleQueries;
import tnt.cqrs_reader.dtos.VehicleDTO;
import tnt.cqrs_reader.dtypes.Position;
import tnt.cqrs_reader.projectors.BaseProjector;
import tnt.cqrs_reader.projectors.VehicleByPositionProjector;
import tnt.cqrs_reader.projectors.VehicleProjector;
import tnt.cqrs_reader.query_repositories.VehiclePositionRepository;
import tnt.cqrs_reader.query_repositories.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class QueryInstance implements VehicleQueries {
    private static final Logger log = LoggerFactory.getLogger(QueryInstance.class);
    private final List<BaseProjector> projectors = new ArrayList<>();
    private VehicleRepository vehicleRepository;
    private VehiclePositionRepository vehicleByPositionRepository;

    public QueryInstance(
            VehicleRepository vehicleRepository,
            VehiclePositionRepository vehicleByPositionRepository,
            VehicleProjector vehicleProjector,
            VehicleByPositionProjector vehicleByPositionProjector) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleByPositionRepository = vehicleByPositionRepository;

        projectors.add(vehicleProjector);
        projectors.add(vehicleByPositionProjector);
    }

    public void startProjectors() {
        for (BaseProjector projector : projectors) {
            Thread projectorThread = new Thread(projector);
            projectorThread.start();
            log.info("Started projector: {}", projector.getClass().getSimpleName());
        }
    }

    @Override
    public VehicleDTO getVehicleByName(String name) {
        return vehicleRepository.getVehicle(name);
    }

    @Override
    public List<VehicleDTO> getVehicles() {
        return vehicleRepository.getAllVehicles();
    }

    @Override
    public List<VehicleDTO> getVehiclesAtPosition(Position position) {
        return vehicleByPositionRepository.getByVehiclePosition(position.x(), position.y());
    }
}