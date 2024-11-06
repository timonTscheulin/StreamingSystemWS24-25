package tnt.cqrs_writer.domain_model.repositories;

public class VehicleMovementRepository {
    private static VehicleMovementRepository INSTANCE;

    private VehicleMovementRepository(){}

    public static VehicleMovementRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VehicleMovementRepository();
        }
        return INSTANCE;
    }
}
