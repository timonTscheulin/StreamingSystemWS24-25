package tnt.cqrs_writer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.repositories.PositionMapRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.framework.CommandHandlerOf;
import tnt.cqrs_writer.framework.events.BaseEvent;

import javax.management.InstanceNotFoundException;
import java.util.List;

@CommandHandlerOf(RemoveVehicle.class)
public class VehicleRemoveHandler implements CommandHandler<RemoveVehicle> {
    private static final Logger log = LoggerFactory.getLogger(VehicleRemoveHandler.class);
    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();
    private final PositionMapRepository positionMapRepository = PositionMapRepository.getInstance();

    @Override
    public List<BaseEvent> handle(RemoveVehicle command) throws InstanceNotFoundException {
        log.debug("Handling RemoveVehicle command for vehicle ID: {}", command.name());

        Vehicle vehicle = vehicleRepository.getVehicle(command.name());

        if (vehicle == null) {
            log.error("Vehicle with ID: {} not exists. Cannot remove vehicle", command.name());
            throw new InstanceNotFoundException("Vehicle with ID " + command.name() + "not exists");
        }

        try {
            List<BaseEvent> events = vehicle.apply(command);
            log.info("Vehicle with ID: {} successfully removed and updated in repository.", command.name());
            return events;
        } catch (Exception e) {
            log.error("An error occurred while applying the RemoveVehicle command for vehicle ID: {}. Error: {}", command.name(), e.getMessage(), e);
            throw e;
        }
    }
}
