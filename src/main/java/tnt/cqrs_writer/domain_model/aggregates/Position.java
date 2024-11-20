package tnt.cqrs_writer.domain_model.aggregates;

import lombok.Getter;
import tnt.cqrs_writer.domain_model.events.position.PositionOccupied;
import tnt.cqrs_writer.domain_model.events.position.PositionReleased;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import java.util.ArrayList;
import java.util.List;

public class Position {
    /* This aggregate is a helper aggregate which stores meta information about the position of each vehicle on a map
     * and allow check if two vehicles collide by a move command or not.
     */
    private AbsolutPosition coordinates;
    @Getter
    private boolean isOccupied = false;
    @Getter
    private String occupiedByVehicle = null;

    public Position(AbsolutPosition position) {
        coordinates = position;
    }


    public List<DomainBaseEvent> occupyPosition(String vehicleId) {
        if (!isOccupied) {
            isOccupied = true;
            occupiedByVehicle = vehicleId;
            List<DomainBaseEvent> events = new ArrayList<>();
            AbsolutPosition copyCoordinates = new AbsolutPosition(this.coordinates.x(), this.coordinates.y());
            events.add(new PositionOccupied(occupiedByVehicle, copyCoordinates));
            return events;
        } else {
            throw new IllegalStateException("Position is occupied and cannot be occupied again.");
        }
    }

    public List<DomainBaseEvent> releasePosition() {
        if (isOccupied) {
            isOccupied = false;
            occupiedByVehicle = null;
            List<DomainBaseEvent> events = new ArrayList<>();
            AbsolutPosition copyCoordinates = new AbsolutPosition(this.coordinates.x(), this.coordinates.y());
            events.add(new PositionReleased(copyCoordinates));
            return events;
        } else {
            throw new IllegalStateException("Position is not occupied and cannot be released.");
        }
    }

    public void replay(PositionOccupied event) {
        occupiedByVehicle = event.getOccupationId();
        isOccupied = true;
    }

    public void replay(PositionReleased event) {
        occupiedByVehicle = null;
        isOccupied = true;
    }
}
