package tnt.cqrs_writer.domain_model.aggregates;

import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.framework.events.BaseEvent;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public class VehicleMovementManager {
    public List<BaseEvent> apply(CreateVehicle command) throws InstanceAlreadyExistsException {
}
