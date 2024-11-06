package tnt.cqrs_writer.domain_model.repositories;

import tnt.cqrs_writer.domain_model.aggregates.VehiclePositionMap;

public class PositionMapRepository {
    private static PositionMapRepository INSTANCE;
    private VehiclePositionMap vehiclePositionMap;
    private PositionMapRepository(){}

    public static PositionMapRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PositionMapRepository();
            VehiclePositionMap vehiclePositionMap = new VehiclePositionMap();
        }
        return INSTANCE;
    }

    public VehiclePositionMap getPositionMap() {
        return vehiclePositionMap;
    }
}
