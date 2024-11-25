package tnt.cqrs_writer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.domain_model.aggregates.VehicleManager;
import tnt.cqrs_writer.domain_model.repositories.PositionRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.framework.CommandHandlerOf;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.EventStore;

import javax.management.InstanceNotFoundException;
import java.util.List;

@CommandHandlerOf(RemoveVehicle.class)
public class VehicleRemoveHandler implements CommandHandler<RemoveVehicle> {
    private static final Logger log = LoggerFactory.getLogger(VehicleRemoveHandler.class);
    private final VehicleRepository vehicleRepository;
    private final PositionRepository positionMapRepository;

    public VehicleRemoveHandler(EventStore eventStore) {
        this.vehicleRepository = new VehicleRepository(eventStore);
        this.positionMapRepository = new PositionRepository(eventStore);
    }

    @Override
    public List<DomainBaseEvent> handle(RemoveVehicle command) throws InstanceNotFoundException {
        log.debug("Handling RemoveVehicle command for vehicle ID: {}", command.name());

        VehicleManager manager = new VehicleManager(vehicleRepository, positionMapRepository);
        try {
            List<DomainBaseEvent> events = manager.apply(command);
            log.info("Vehicle with ID: {} successfully removed and updated in repository.", command.name());
            return events;
        } catch (Exception e) {
            log.error("An error occurred while applying the RemoveVehicle command for vehicle ID: {}. Error: {}", command.name(), e.getMessage(), e);
            throw e;
        }
    }
}
