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

        // validate create command
        Vehicle vehicle = vehicleRepository.getVehicle(command.name());
        List<DomainBaseEvent> events = new ArrayList<>(vehicle.apply(command));

        // validate new vehicle target position
        AbsolutPosition vehicleTargetPosition = vehicle.getCurrentPosition();
        Position targetPosition = positionRepository.getPosition(vehicleTargetPosition);
        events.addAll(targetPosition.apply(command));

        return events;
    }

    public List<DomainBaseEvent> apply(MoveVehicle command) {

        //validate vehicle move command
        Vehicle vehicle = vehicleRepository.getVehicle(command.name());
        AbsolutPosition vehicleOldPosition = vehicle.getCurrentPosition();
        List<DomainBaseEvent> events = new ArrayList<>(vehicle.apply(command));

        //check vehicle still exist.
        if (vehicle.exists()) {
            AbsolutPosition vehicleTargetPosition = vehicle.getCurrentPosition();
            Position targetPosition = positionRepository.getPosition(vehicleTargetPosition);
            events.addAll(targetPosition.apply(command));
        }
        else {
            Position oldPosition = positionRepository.getPosition(vehicleOldPosition);

            // hier müsste nun eigentlich die alte position wieder freigegeben werden,
            // da das vehicle seinen 20 zug ereicht hatte und damit gelösct wird.
        }

        return events;
    }

    public List<DomainBaseEvent> apply(RemoveVehicle command) {
        // validate vehicle remove command
        Vehicle vehicle = vehicleRepository.getVehicle(command.name());
        List<DomainBaseEvent> events = new ArrayList<>(vehicle.apply(command));

        // delete vehicle on current destination
        AbsolutPosition vehicleTargetPosition = vehicle.getCurrentPosition();
        Position targetPosition = positionRepository.getPosition(vehicleTargetPosition);
        events.addAll(targetPosition.apply(command));
        return events;
    }
}
