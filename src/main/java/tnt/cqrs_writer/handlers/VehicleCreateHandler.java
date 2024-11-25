package tnt.cqrs_writer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.aggregates.VehicleManager;
import tnt.cqrs_writer.domain_model.repositories.PositionRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.framework.CommandHandlerOf;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

@CommandHandlerOf(CreateVehicle.class)
public class VehicleCreateHandler implements CommandHandler<CreateVehicle> {
    private static final Logger log = LoggerFactory.getLogger(VehicleCreateHandler.class);
    private final VehicleRepository vehicleRepository;
    private final PositionRepository positionMapRepository;

    public VehicleCreateHandler(VehicleRepository vehicleRepository, PositionRepository positionMapRepository) {
        this.vehicleRepository = vehicleRepository;
        this.positionMapRepository = positionMapRepository;
    }

    @Override
    public List<DomainBaseEvent> handle(CreateVehicle command) throws InstanceAlreadyExistsException {
        log.debug("Handling CreateVehicle command for vehicle ID: {}", command.name());

        VehicleManager manager = new VehicleManager(vehicleRepository, positionMapRepository);
        try {
            //List<DomainBaseEvent> events = vehicle.apply(command);
            List<DomainBaseEvent> events = manager.apply(command);
            log.info("Vehicle with ID: {} successfully created and updated in repository.", command.name());

            return events;
        } catch (Exception e) {
            log.error("An error occurred while applying the CreateVehicle command for vehicle ID: {}. Error: {}", command.name(), e.getMessage(), e);
            throw e;
        }
    }
}
