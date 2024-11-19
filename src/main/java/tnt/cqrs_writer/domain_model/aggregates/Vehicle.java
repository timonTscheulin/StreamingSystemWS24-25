package tnt.cqrs_writer.domain_model.aggregates;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleCreated;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleHasReachedMovementLimit;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleNewPosition;
import tnt.cqrs_writer.domain_model.events.vehicle.VehicleRemoved;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import javax.management.InstanceAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Vehicle {
    /* This is the primary aggregate wich represents vehicles. It encapsulates the application logic, the data and
     * event creation as a single logical unit.
     */
    private static final Logger log = LoggerFactory.getLogger(Vehicle.class);
    @Getter
    private String vehicleId;
    private List<AbsolutPosition> vehiclePositions = new ArrayList<>();
    private int moveCounter = 0;
    private boolean exists = false;

    public Vehicle(String vehicleId) {
        this.vehicleId = vehicleId;
        log.info("Vehicle created with id: {}", vehicleId);
    }

    public List<DomainBaseEvent> apply(CreateVehicle command) throws InstanceAlreadyExistsException {
        return createVehicle(command.startPosition().x(), command.startPosition().y());
    }

    public List<DomainBaseEvent> apply(MoveVehicle command) {
        return moveVehicle(command.deltaPosition().x(), command.deltaPosition().y());
    }

    public List<DomainBaseEvent> apply(RemoveVehicle command) {
        return this.removeVehicle();
    }

    public List<DomainBaseEvent> createVehicle(int start_x, int start_y) throws InstanceAlreadyExistsException {
        log.debug("Applying CreateVehicle command for vehicle id: {}", vehicleId);
        List<DomainBaseEvent> events = new ArrayList<>();

        // validate by apply steps to aggregate
        if (!exists) {
            exists = true;
            vehiclePositions.add(new AbsolutPosition(start_x, start_y));
            log.info("Vehicle created with id: {} at position: {}", vehicleId, vehiclePositions.getFirst());
        }
        else {
            log.error("CreateVehicle command failed for vehicle id: {}. Vehicle already exists.", vehicleId);
            throw new InstanceAlreadyExistsException("Can not apply Create command. Vehicle with id " + vehicleId + " already exists.");
        }

        // add event if no error was thrown
        events.add(new VehicleCreated(vehicleId, start_x, start_y));

        return events;
    }


    public List<DomainBaseEvent> moveVehicle(int delta_x, int delta_y) {
        log.debug("Applying MoveVehicle command for vehicle id: {}", vehicleId);
        List<DomainBaseEvent> events = new ArrayList<>();

        if(!exists) {
            log.error("MoveVehicle command failed for vehicle id: {}. Vehicle does not exist.", vehicleId);
            throw new NoSuchElementException("Can not apply Move command. Vehicle with id " + vehicleId + " not exists.");
        }

        // solves task 3.1
        if(moveCounter + 1 >= 20) {
            //return this.apply(new RemoveVehicle(vehicleId));
            events.add(new VehicleHasReachedMovementLimit(vehicleId));
            return events;
        }

        int newAbsX = vehiclePositions.getLast().x() + delta_x;
        int newAbsY = vehiclePositions.getLast().y() + delta_y;
        AbsolutPosition newPosition = new AbsolutPosition(newAbsX, newAbsY);

        // solves task 3.2
        if (vehiclePositions.contains(newPosition)) {
            // return this.apply(new RemoveVehicle(vehicleId));
            events.add(new VehicleHasReachedMovementLimit(vehicleId));
            return events;
        }

        vehiclePositions.add(newPosition);
        // solves task 3.1
        moveCounter++;

        log.info("Vehicle with id: {} moved to new position: {}", vehicleId, vehiclePositions.getLast());
        events.add(new VehicleNewPosition(vehicleId, newAbsX, newAbsY));

        return events;
    }


    public List<DomainBaseEvent> removeVehicle() {

        log.debug("Applying RemoveVehicle command for vehicle id: {}", vehicleId);
        List<DomainBaseEvent> events = new ArrayList<>();

        if(!exists) {
            log.error("RemoveVehicle command failed for vehicle id: {}. Vehicle does not exist.", vehicleId);
            throw new NoSuchElementException("Can not apply Delete command. Vehicle with id " + vehicleId + " not exists.");
        }

        exists = false;
        vehiclePositions = new ArrayList<>();
        moveCounter = 0;
        log.info("Vehicle with id: {} has been removed", vehicleId);
        events.add(new VehicleRemoved(vehicleId));

        return events;
    }

    public void replay(VehicleCreated event) {
        log.info("Replaying create vehicle event: {}", event.toString());
        exists = true;
        vehiclePositions.add(new AbsolutPosition(event.getStartPosition().x(), event.getStartPosition().y()));
    }

    public void replay(VehicleNewPosition event) {
        log.info("Replaying move vehicle event: {}", event.toString());

        AbsolutPosition newPosition = event.getPosition();
        vehiclePositions.add(newPosition);
        moveCounter++;
    }

    public void replay(VehicleRemoved event) {
        log.info("Replaying remove vehicle event: {}", event.toString());
        exists = false;
        vehiclePositions = new ArrayList<>();
        moveCounter = 0;
    }

    // helper function to allow in memory repository to delete not existing aggregates.
    public boolean exists() {
        return exists;
    }
    public AbsolutPosition getCurrentPosition() {return this.vehiclePositions.getLast();};
}
