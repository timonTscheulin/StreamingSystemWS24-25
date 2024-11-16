package tnt.cqrs_writer.domain_model.aggregates;


import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.domain_model.repositories.PositionRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;


import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class VehicleManager {
    /* This aggregate is a facade which hides the direct access to specific vehicle aggregates.
     * It contains no data only the logic which is necessary to ensure data consistency over many aggregates
     * if one command changes more than one aggregate at once.
     */
    private final PositionRepository positionRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository, PositionRepository positionMapRepository) {
        this.vehicleRepository = vehicleRepository;
        this.positionRepository = positionMapRepository;
    }

    public List<DomainBaseEvent> apply(CreateVehicle command) throws InstanceAlreadyExistsException {
        List<DomainBaseEvent> events = new ArrayList<>();

        // validate create command
        Vehicle vehicle = vehicleRepository.getVehicle(command.name());
        events.addAll(vehicle.apply(command));

        // validate new vehicle target position
        AbsolutPosition targetPosition = vehicle.getCurrentPosition();
        positionRepository.getPosition(targetPosition);


        return events;
    }

    public List<DomainBaseEvent> apply(MoveVehicle command) {
        return null;
    }

    public List<DomainBaseEvent> apply(RemoveVehicle command) {
        return null;
    }
}
