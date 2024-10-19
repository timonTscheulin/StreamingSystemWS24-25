package tnt.cqrs_writer.handlers;

import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.framework.CommandHandlerOf;

@CommandHandlerOf(CreateVehicle.class)
public class VehicleCreateHandler implements CommandHandler<CreateVehicle> {
    @Override
    public void handle(CreateVehicle command) {
        System.out.println("Vehicle created command received");
    }
}
