package tnt.eventstore.event_contract.position;

import tnt.cqrs_writer.domain_model.events.position.PositionOccupied;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

public class StorePositionOccupied extends StoreBasePosition {

    public StorePositionOccupied(int x, int y) {
        super(x, y);
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new PositionOccupied(new AbsolutPosition(getX_position(), getY_position()));
    }
}
