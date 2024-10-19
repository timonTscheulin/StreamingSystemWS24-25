package tnt.cqrs_writer.commands;

import tnt.cqrs_writer.dtypes.Position;

public record CreateVehicle(String name, Position startPosition) implements Command { }
