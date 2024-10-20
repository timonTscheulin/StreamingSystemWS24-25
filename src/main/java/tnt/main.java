package tnt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.api.SimpleCommandsApi;
import tnt.cqrs_writer.dtypes.PositionDelta;
import tnt.cqrs_writer.dtypes.PositionPoint;

public class main {
    private static final Logger log = LoggerFactory.getLogger(main.class);

    public static void main(String[] args) throws Exception {
        SimpleCommandsApi api = new SimpleCommandsApi();
        api.createVehicle("Test", new PositionPoint(0,0));
        api.moveVehicle("Test", new PositionDelta(0,0));
        api.removeVehicle("Test");
    }
}
