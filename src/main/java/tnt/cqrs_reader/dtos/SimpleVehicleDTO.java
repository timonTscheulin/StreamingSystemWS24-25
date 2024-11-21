package tnt.cqrs_reader.dtos;

import tnt.cqrs_reader.dtypes.Position;

public class SimpleVehicleDTO implements VehicleDTO {
    private Position position;
    private String vehicleId;
    private int moves;

    public SimpleVehicleDTO(String vehicleId, int x, int y, int moves) {
        position = new Position(x, y);
        this.vehicleId = vehicleId;
        this.moves = moves;
    }

    @Override
    public String getName() {
        return this.vehicleId;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getNumberOfMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return "Simple Vehicle Id: " + vehicleId + " position: " + position;
    }
}
