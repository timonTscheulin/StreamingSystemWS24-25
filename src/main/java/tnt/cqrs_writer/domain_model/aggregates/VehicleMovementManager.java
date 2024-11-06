package tnt.cqrs_writer.domain_model.aggregates;

import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.framework.events.BaseEvent;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public class VehicleMovementManager {
    /* This aggregate is a facade which hides the direct access to specific vehicle aggregates.
     * It contains no data only the logic which is necessary to ensure data consistency over many aggregates
     * if one command changes more than one aggregate at once.
     */
    //public List<BaseEvent> apply(CreateVehicle command) throws InstanceAlreadyExistsException {
}
