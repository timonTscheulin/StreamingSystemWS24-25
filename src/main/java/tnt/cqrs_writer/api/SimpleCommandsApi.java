package tnt.cqrs_writer.api;

import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.framework.CommandDispatcher;
import tnt.cqrs_writer.dtypes.Position;

public class SimpleCommandsApi implements VehicleCommands {
    CommandDispatcher commandDispatcher = new CommandDispatcher();

    @Override
    public void createVehicle(String name, Position startPosition) throws Exception {
        CreateVehicle command = new CreateVehicle(name, startPosition);
        commandDispatcher.dispatch(command);
    }

    @Override
    public void moveVehicle(String name, Position moveVector) throws Exception {
        MoveVehicle command = new MoveVehicle(name, moveVector);
        commandDispatcher.dispatch(command);
    }

    @Override
    public void removeVehicle(String name) throws Exception {
        RemoveVehicle command = new RemoveVehicle(name);
        commandDispatcher.dispatch(command);
    }
}
