package tnt.cqrs_writer.api;

import tnt.cqrs_writer.dtypes.PositionDelta;
import tnt.cqrs_writer.dtypes.PositionPoint;

public interface VehicleCommands {
    void createVehicle(String name, PositionPoint startPosition) throws Exception;
    void moveVehicle(String name, PositionDelta moveVector) throws Exception;
    void removeVehicle(String name) throws Exception;
}
