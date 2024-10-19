package tnt.cqrs_reader.api;

import tnt.cqrs_reader.dtos.VehicleDTO;
import tnt.cqrs_reader.dtypes.Position;

import java.util.List;

public interface VehicleQueries {
    public VehicleDTO getVehicleByName(String name);
    public List<VehicleDTO> getVehicles();
    public List<VehicleDTO> getVehiclesAtPosition(Position position);
}
