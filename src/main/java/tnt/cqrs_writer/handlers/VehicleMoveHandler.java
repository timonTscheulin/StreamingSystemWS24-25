package tnt.cqrs_writer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.domain_model.aggregates.Vehicle;
import tnt.cqrs_writer.domain_model.aggregates.VehicleManager;
import tnt.cqrs_writer.domain_model.repositories.PositionRepository;
import tnt.cqrs_writer.domain_model.repositories.VehicleRepository;
import tnt.cqrs_writer.framework.CommandHandlerOf;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Component
@CommandHandlerOf(MoveVehicle.class)
public class git pushVehicleMoveHandler implements CommandHandler<MoveVehicle> {
    private static final Logger log = LoggerFactory.getLogger(VehicleMoveHandler.class);
    private final VehicleRepository vehicleRepository;
    private final PositionRepository positionMapRepository;

    public VehicleMoveHandler(VehicleRepository vehicleRepository, PositionRepository positionMapRepository) {
        this.vehicleRepository = vehicleRepository;
        this.positionMapRepository = positionMapRepository;
    }

    @Override
    public List<DomainBaseEvent> handle(MoveVehicle command) throws InstanceNotFoundException {

        // simple gatekeeper to prevent unnecessary aggregate loads, if trivial properties contain wrong values.
        if (command.deltaPosition().isZero()) {
            throw new IllegalArgumentException("Move vector cannot be zero");
        }

        log.debug("Handling MoveVehicle command for vehicle ID: {}", command.name());

        VehicleManager manager = new VehicleManager(vehicleRepository, positionMapRepository);

        try {
            List<DomainBaseEvent> events = manager.apply(command);
            log.info("Vehicle with ID: {} successfully moved and updated in repository.", command.name());
            return events;
        } catch (Exception e) {
            log.error("An error occurred while applying the MoveVehicle command for vehicle ID: {}. Error: {}", command.name(), e.getMessage(), e);
            throw e;
        }
    }
}
