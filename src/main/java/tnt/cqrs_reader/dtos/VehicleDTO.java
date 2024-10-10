package tnt.cqrs_reader.dtos;

import tnt.dtypes.Position;

public interface VehicleDTO {
    public String getName();
    public Position getPosition();
    public int getNumberOfMoves();
}