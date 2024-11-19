package tnt.cqrs_writer.domain_model.aggregates;

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
    private String position;
    private AbsolutPosition coordinates;
    private boolean isOccupied = false;

    public List<DomainBaseEvent> occupyPosition() {
        if (!isOccupied) {
            isOccupied = true;
            List<DomainBaseEvent> events = new ArrayList<>();
            AbsolutPosition copyCoordinates = new AbsolutPosition(this.coordinates.x(), this.coordinates.y());
            events.add(new PositionOccupied(copyCoordinates));
            return events;
        } else {
            throw new IllegalStateException("Position is occupied and cannot be occupied again.");
        }
    }

    public List<DomainBaseEvent> releasePosition() {
        if (isOccupied) {
            isOccupied = false;
            List<DomainBaseEvent> events = new ArrayList<>();
            AbsolutPosition copyCoordinates = new AbsolutPosition(this.coordinates.x(), this.coordinates.y());
            events.add(new PositionReleased(copyCoordinates));
            return events;
        } else {
            throw new IllegalStateException("Position is not occupied and cannot be released.");
        }
    }
}
