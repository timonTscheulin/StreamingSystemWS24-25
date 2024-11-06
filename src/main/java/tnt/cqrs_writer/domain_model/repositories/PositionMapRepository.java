package tnt.cqrs_writer.domain_model.repositories;

public class PositionMapRepository {
    private static PositionMapRepository INSTANCE;

    private PositionMapRepository(){}

    public static PositionMapRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PositionMapRepository();
        }
        return INSTANCE;
    }
}
