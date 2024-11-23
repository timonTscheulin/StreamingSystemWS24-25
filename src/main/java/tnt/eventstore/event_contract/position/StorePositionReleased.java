package tnt.eventstore.event_contract.position;

import tnt.cqrs_writer.domain_model.events.position.PositionReleased;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

public class StorePositionReleased extends StoreBasePosition {
    public StorePositionReleased(int x, int y) {
        super(x, y);
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new PositionReleased(new AbsolutPosition(getX_position(), getY_position()));
    }

    @Override
    public String getEventType() {
        return "StorePositionReleased";
    }
}
