package tnt;

import tnt.cqrs_writer.api.SimpleCommandsApi;
import tnt.cqrs_writer.dtypes.Position;

public class main {
    public static void main(String[] args) throws Exception {
        SimpleCommandsApi api = new SimpleCommandsApi();
        api.createVehicle("Test", new Position(0,0));
        api.moveVehicle("Test", new Position(0,0));
        api.removeVehicle("Test");
    }
}
