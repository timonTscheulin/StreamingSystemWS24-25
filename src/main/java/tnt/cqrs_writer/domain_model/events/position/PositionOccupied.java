package tnt.cqrs_writer.domain_model.events.position;

import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.position.StorePositionOccupied;

public class PositionOccupied implements DomainBaseEvent {
    private AbsolutPosition position;

    public PositionOccupied(AbsolutPosition position) {
        this.position = position;
    }

    @Override
    public StoreBaseEvent toStoreEvent() {
        return new StorePositionOccupied(position.x(), position.y());
    }
}
