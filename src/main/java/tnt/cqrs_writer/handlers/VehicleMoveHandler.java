package tnt.cqrs_writer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.repositories.PositionMapRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleMovementRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.framework.CommandHandlerOf;
import tnt.cqrs_writer.framework.events.BaseEvent;

import javax.management.InstanceNotFoundException;
import java.util.List;

@CommandHandlerOf(MoveVehicle.class)
public class VehicleMoveHandler implements CommandHandler<MoveVehicle> {
    private static final Logger log = LoggerFactory.getLogger(VehicleMoveHandler.class);
    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();
    private final PositionMapRepository positionMapRepository = PositionMapRepository.getInstance();
    private final VehicleMovementRepository vehicleMovementRepository = VehicleMovementRepository.getInstance();

    @Override
    public List<BaseEvent> handle(MoveVehicle command) throws InstanceNotFoundException {

        // simple gatekeeper to prevent unnecessary aggregate loads, if trivial properties contain wrong values.
        if (command.deltaPosition().isZero()) {
            throw new IllegalArgumentException("Move vector cannot be zero");
        }

        log.debug("Handling MoveVehicle command for vehicle ID: {}", command.name());

        Vehicle vehicle = vehicleRepository.getVehicle(command.name());

        if (vehicle == null) {
            log.error("Vehicle with ID: {} not exists. Cannot move vehicle", command.name());
            throw new InstanceNotFoundException("Vehicle with ID " + command.name() + "not exists");
        }

        try {
            List<BaseEvent> events = vehicle.apply(command);
            vehicleRepository.updateVehicle(vehicle);
            log.info("Vehicle with ID: {} successfully moved and updated in repository.", command.name());
            return events;
        } catch (Exception e) {
            log.error("An error occurred while applying the MoveVehicle command for vehicle ID: {}. Error: {}", command.name(), e.getMessage(), e);
            throw e;
        }
    }
}
