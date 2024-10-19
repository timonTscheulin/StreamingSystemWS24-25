package tnt.cqrs_reader.dtos;

import tnt.cqrs_reader.dtypes.Position;

public interface VehicleDTO {
    public String getName();
    public Position getPosition();
    public int getNumberOfMoves();
}