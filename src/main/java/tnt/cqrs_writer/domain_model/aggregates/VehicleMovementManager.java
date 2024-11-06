package tnt.cqrs_writer.domain_model.aggregates;


import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.domain_model.repositories.PositionMapRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.framework.events.BaseEvent;


import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class VehicleMovementManager {
    /* This aggregate is a facade which hides the direct access to specific vehicle aggregates.
     * It contains no data only the logic which is necessary to ensure data consistency over many aggregates
     * if one command changes more than one aggregate at once.
     */
    private final PositionMapRepository positionMapRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleMovementManager(VehicleRepository vehicleRepository, PositionMapRepository positionMapRepository) {
        this.vehicleRepository = vehicleRepository;
        this.positionMapRepository = positionMapRepository;
    }

    public List<BaseEvent> apply(CreateVehicle command) throws InstanceAlreadyExistsException {

        List<BaseEvent> events = new ArrayList<>();
        try {
            Vehicle vehicle = vehicleRepository.getVehicle(command.name());
            VehiclePositionMap map = positionMapRepository.getPositionMap();
            // problem: this solution requires the event reply mechanism to synchronize the two different aggregates
            events.addAll(vehicle.apply(command));
            events.addAll(map.apply(command));
        } catch (Exception e) {
            throw e;
        }

        return events;
    }

    public List<BaseEvent> apply(MoveVehicle command) {
        return null;
    }

    public List<BaseEvent> apply(RemoveVehicle command) {
        return null;
    }
}
