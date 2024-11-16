package tnt.cqrs_writer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.repositories.PositionRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.framework.CommandHandlerOf;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

@CommandHandlerOf(CreateVehicle.class)
public class VehicleCreateHandler implements CommandHandler<CreateVehicle> {
    private static final Logger log = LoggerFactory.getLogger(VehicleCreateHandler.class);
    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();
    private final PositionRepository positionMapRepository = PositionRepository.getInstance();

    @Override
    public List<DomainBaseEvent> handle(CreateVehicle command) throws InstanceAlreadyExistsException {
        log.debug("Handling CreateVehicle command for vehicle ID: {}", command.name());

        Vehicle vehicle = vehicleRepository.getVehicle(command.name());

        if (vehicle == null) {
            log.error("Inconsistent repository state. Something realy bad was happened at command {}.", command.name());
            throw new IllegalStateException("Command can not be executed caused by illegal repository state");
        }

        if (vehicle.exists()) {
            log.error("Vehicle with ID: {} already exists. Cannot create new vehicle.", command.name());
            throw new InstanceAlreadyExistsException("Vehicle with ID " + command.name() + " already exists.");
        }

        vehicle = new Vehicle(command.name());
        try {
            List<DomainBaseEvent> events = vehicle.apply(command);
            log.info("Vehicle with ID: {} successfully created and updated in repository.", command.name());

            return events;
        } catch (Exception e) {
            log.error("An error occurred while applying the CreateVehicle command for vehicle ID: {}. Error: {}", command.name(), e.getMessage(), e);
            throw e;
        }
    }
}
