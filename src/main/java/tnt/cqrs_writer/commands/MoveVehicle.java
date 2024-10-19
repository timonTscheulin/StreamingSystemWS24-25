package tnt.cqrs_writer.commands;

import tnt.cqrs_writer.dtypes.Position;

public record MoveVehicle(String name, Position moveVector) implements Command {}
