package tnt.cqrs_writer.domain_model.aggregates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_writer.commands.CreateVehicle;
import tnt.cqrs_writer.commands.MoveVehicle;
import tnt.cqrs_writer.commands.RemoveVehicle;
import tnt.cqrs_writer.domain_model.events.VehicleCreated;
import tnt.cqrs_writer.domain_model.events.VehicleMoved;
import tnt.cqrs_writer.domain_model.events.VehicleRemoved;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.BaseEvent;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Vehicle {
    private static final Logger log = LoggerFactory.getLogger(Vehicle.class);
    private String vehicleId;
    private AbsolutPosition vehiclePosition;
    private boolean exists = false;

    public Vehicle(String vehicleId) {
        this.vehicleId = vehicleId;
        log.info("Vehicle created with id: {}", vehicleId);
    }

    public Vehicle(Vehicle vehicleToCopy) {
        this.vehicleId = vehicleToCopy.getVehicleId();
        log.info("Vehicle copied with id: {}", vehicleId);
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public List<BaseEvent> apply(CreateVehicle command) throws InstanceAlreadyExistsException {
        log.debug("Applying CreateVehicle command for vehicle id: {}", vehicleId);
        List<BaseEvent> events = new ArrayList<>();

        // validate by apply steps to aggregate
        if (!exists) {
            exists = true;
            vehiclePosition = new AbsolutPosition(command.startPosition().x(), command.startPosition().y());
            log.info("Vehicle created with id: {} at position: {}", vehicleId, vehiclePosition);
        }
        else {
            log.error("CreateVehicle command failed for vehicle id: {}. Vehicle already exists.", vehicleId);
            throw new InstanceAlreadyExistsException("Can not apply Create command. Vehicle with id " + vehicleId + " already exists.");
        }

        // add event if no error was thrown
        events.add(new VehicleCreated(vehicleId));

        return events;
    }

    public List<BaseEvent> apply(MoveVehicle command) {
        log.debug("Applying MoveVehicle command for vehicle id: {}", vehicleId);
        List<BaseEvent> events = new ArrayList<>();

        if(!exists) {
            log.error("MoveVehicle command failed for vehicle id: {}. Vehicle does not exist.", vehicleId);
            throw new NoSuchElementException("Can not apply Move command. Vehicle with id " + vehicleId + " not exists.");
        }

        int newAbsX = vehiclePosition.x() + command.deltaPosition().x();
        int newAbsY = vehiclePosition.y() + command.deltaPosition().y();

        vehiclePosition = new AbsolutPosition(newAbsX, newAbsY);

        log.info("Vehicle with id: {} moved to new position: {}", vehicleId, vehiclePosition);
        events.add(new VehicleMoved(vehicleId, new AbsolutPosition(newAbsX, newAbsY)));

        return events;
    }

    public List<BaseEvent> apply(RemoveVehicle command) {
        log.debug("Applying RemoveVehicle command for vehicle id: {}", vehicleId);
        List<BaseEvent> events = new ArrayList<>();

        if(!exists) {
            log.error("RemoveVehicle command failed for vehicle id: {}. Vehicle does not exist.", vehicleId);
            throw new NoSuchElementException("Can not apply Delete command. Vehicle with id " + vehicleId + " not exists.");
        }

        exists = false;
        log.info("Vehicle with id: {} has been removed", vehicleId);
        events.add(new VehicleRemoved(vehicleId));

        return events;
    }

    // helper function to allow in memory repository to delete not existing aggregates.
    public boolean exists() {
        return exists;
    }
}
