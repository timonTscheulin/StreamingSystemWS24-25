package tnt.cqrs_writer.domain_model.aggregates;

import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.framework.events.BaseEvent;

import java.util.List;

public class VehiclePositionMap {
    /* This aggregate is a helper aggregate which stores meta information about the position of each vehicle on a map
     * and allow check if two vehicles collide by a move command or not.
     */

    public List<BaseEvent> apply(CreateVehicle command) {
        return null;
    }

    public List<BaseEvent> apply(MoveVehicle command) {
        return null;
    }

    public List<BaseEvent> apply(RemoveVehicle command) {
        return null;
    }
}
