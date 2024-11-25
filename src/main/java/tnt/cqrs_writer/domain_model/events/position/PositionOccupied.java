package tnt.cqrs_writer.domain_model.events.position;

import lombok.Getter;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.position.StorePositionOccupied;

@Getter
public class PositionOccupied implements DomainBaseEvent {
    private final AbsolutPosition position;
    private final String occupationId;

    public PositionOccupied(String occupiedBy, AbsolutPosition position) {
        this.position = position;
        this.occupationId = occupiedBy;
    }

    @Override
    public StoreBaseEvent toStoreEvent() {
        return new StorePositionOccupied(occupationId, position.x(), position.y());
    }
}
