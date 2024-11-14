package tnt.cqrs_writer.domain_model.aggregates;

import junit.framework.TestCase;
import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.domain_model.events.VehicleCreated;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.dtypes.PositionPoint;
import tnt.cqrs_writer.framework.events.BaseEvent;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public class VehicleTest extends TestCase {

    public void testCreateVehicle() throws InstanceAlreadyExistsException {
        // Testdaten vorbereiten
        String vehicleId = "testVehicle";
        int startX = 0;
        int startY = 0;
        CreateVehicle command = new CreateVehicle(vehicleId, new PositionPoint(startX, startY));

        // Fahrzeug erstellen
        Vehicle vehicle = new Vehicle(vehicleId);
        List<BaseEvent> events = vehicle.apply(command);

        // Überprüfen, ob das CreateVehicle-Event korrekt verarbeitet wurde
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof VehicleCreated);

        // Überprüfen, ob das Fahrzeug korrekt initialisiert wurde
        VehicleCreated createdEvent = (VehicleCreated) events.get(0);
        assertEquals(vehicleId, createdEvent.vehicleId());
        assertEquals(startX, createdEvent.getStartPosition().x());
        assertEquals(startY, createdEvent.getStartPosition().y());

        // Überprüfen, ob das Fahrzeug-Objekt in einem konsistenten Zustand ist
        assertTrue(vehicle.exists());
        assertEquals(vehicleId, vehicle.getVehicleId());
        //assertEquals(1, vehicle.().size());
        //assertEquals(new AbsolutPosition(startX, startY), events.getFirst());
    }

}