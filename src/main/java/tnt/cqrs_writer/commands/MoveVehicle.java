package tnt.cqrs_writer.commands;

import tnt.cqrs_writer.dtypes.PositionDelta;

public record MoveVehicle(String name, PositionDelta deltaPosition) implements Command {}
