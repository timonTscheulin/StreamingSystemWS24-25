package tnt.cqrs_writer.domain_model.aggregates;

import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import java.util.List;

public class Position {
    /* This aggregate is a helper aggregate which stores meta information about the position of each vehicle on a map
     * and allow check if two vehicles collide by a move command or not.
     */
    private String position;

    public List<DomainBaseEvent> apply(CreateVehicle command) {
        return null;
    }

    public List<DomainBaseEvent> apply(MoveVehicle command) {
        return null;
    }

    public List<DomainBaseEvent> apply(RemoveVehicle command) {
        return null;
    }
}
