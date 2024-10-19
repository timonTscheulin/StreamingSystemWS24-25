package tnt.cqrs_writer.commands;

import tnt.cqrs_writer.dtypes.PositionPoint;

public record CreateVehicle(String name, PositionPoint startPosition) implements Command { }
