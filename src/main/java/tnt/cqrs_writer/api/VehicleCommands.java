package tnt.cqrs_writer.api;

import tnt.dtypes.Position;

public interface VehicleCommands {
    void createVehicle(String name, Position startPosition) throws Exception;
    void moveVehicle(String name, Position moveVector) throws Exception;
    void removeVehicle(String name) throws Exception;
}
