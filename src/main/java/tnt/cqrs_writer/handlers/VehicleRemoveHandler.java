package tnt.cqrs_writer.handlers;

import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.framework.CommandHandlerOf;

@CommandHandlerOf(RemoveVehicle.class)
public class VehicleRemoveHandler implements CommandHandler<RemoveVehicle> {
    @Override
    public void handle(RemoveVehicle command) {
        System.out.println("Vehicle removed");
    }
}
