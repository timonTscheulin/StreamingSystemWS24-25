package tnt.cqrs_writer.domain_model.aggregates;


import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleHasReachedMovementLimit;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleHasVisitedPositionAgain;
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


    // do we need some were a check if vehicle exists. check the logic of the aggregates again.
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
        events.addAll(vehicle.createVehicle(command.startPosition().x(), command.startPosition().y()));

        // validate new vehicle target position
        AbsolutPosition vehicleTargetPosition = vehicle.getCurrentPosition();
        Position targetPosition = positionRepository.getPosition(vehicleTargetPosition);
        events.addAll(targetPosition.occupyPosition());

        return events;
    }

    public List<DomainBaseEvent> apply(MoveVehicle command) {
        List<DomainBaseEvent> events = new ArrayList<>();

        //validate vehicle move command
        Vehicle vehicle = vehicleRepository.getVehicle(command.name());
        AbsolutPosition vehicleOldPosition = vehicle.getCurrentPosition();
        events.addAll(vehicle.moveVehicle(command.deltaPosition().x(), command.deltaPosition().y()));

        //check vehicle still exist.
        if (containsEventOfType(events, VehicleHasReachedMovementLimit.class)
                || containsEventOfType(events, VehicleHasVisitedPositionAgain.class))
        {
            Position oldPosition = positionRepository.getPosition(vehicleOldPosition);
            events.addAll(oldPosition.releasePosition());
            events.addAll(vehicle.removeVehicle());
        }
        else {
            AbsolutPosition vehicleTargetPosition = vehicle.getCurrentPosition();
            Position oldPosition = positionRepository.getPosition(vehicleOldPosition);
            Position targetPosition = positionRepository.getPosition(vehicleTargetPosition);

            events.addAll(oldPosition.releasePosition());
            events.addAll(targetPosition.occupyPosition());
        }
        return events;
    }

    public List<DomainBaseEvent> apply(RemoveVehicle command) {
        List<DomainBaseEvent> events = new ArrayList<>();
        // validate vehicle remove command
        Vehicle vehicle = vehicleRepository.getVehicle(command.name());
        events.addAll(vehicle.removeVehicle());

        // delete vehicle on current destination
        AbsolutPosition vehicleTargetPosition = vehicle.getCurrentPosition();
        Position targetPosition = positionRepository.getPosition(vehicleTargetPosition);
        events.addAll(targetPosition.releasePosition());
        return events;
    }

    private static boolean containsEventOfType(List<DomainBaseEvent> events, Class<?> eventType) {
        return events.stream().anyMatch(eventType::isInstance);
    }
}
