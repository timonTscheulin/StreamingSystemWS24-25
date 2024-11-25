package tnt.cqrs_writer.api;

import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.dtypes.PositionDelta;
import tnt.cqrs_writer.dtypes.PositionPoint;
import tnt.cqrs_writer.framework.CommandDispatcher;
import tnt.eventstore.EventStore;

public class SimpleCommandsApi implements VehicleCommands {
    private final CommandDispatcher commandDispatcher;

    public SimpleCommandsApi(EventStore eventStore) {
        this.commandDispatcher = new CommandDispatcher(eventStore);
    }

    @Override
    public void createVehicle(String name, PositionPoint startPosition) throws Exception {
        CreateVehicle command = new CreateVehicle(name, startPosition);
        commandDispatcher.dispatch(command);
    }

    @Override
    public void moveVehicle(String name, PositionDelta moveVector) throws Exception {
        MoveVehicle command = new MoveVehicle(name, moveVector);
        commandDispatcher.dispatch(command);
    }

    @Override
    public void removeVehicle(String name) throws Exception {
        RemoveVehicle command = new RemoveVehicle(name);
        commandDispatcher.dispatch(command);
    }
}
