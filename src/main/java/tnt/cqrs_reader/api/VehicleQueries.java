package tnt.cqrs_reader.api;

import tnt.cqrs_reader.dtos.VehicleDTO;
import tnt.dtypes.Position;

import java.util.Enumeration;

public interface VehicleQueries {
    public VehicleDTO getVehicleByName(String name);
    public Enumeration<VehicleDTO> getVehicles();
    public Enumeration<VehicleDTO> getVehiclesAtPosition(Position position);
}
