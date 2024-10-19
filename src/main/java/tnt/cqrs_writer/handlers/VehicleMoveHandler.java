package tnt.cqrs_writer.handlers;

import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.framework.CommandHandlerOf;

@CommandHandlerOf(MoveVehicle.class)
public class VehicleMoveHandler implements CommandHandler<MoveVehicle> {
    @Override
    public void handle(MoveVehicle command) {
        System.out.println("Move Vehicle received");
    }
}
