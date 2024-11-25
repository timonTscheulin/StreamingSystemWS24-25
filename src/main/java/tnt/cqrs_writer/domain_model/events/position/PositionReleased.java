package tnt.cqrs_writer.domain_model.events.position;

import lombok.Getter;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.position.StorePositionReleased;

@Getter
public class PositionReleased implements DomainBaseEvent {
    private AbsolutPosition position;

    public PositionReleased(AbsolutPosition position) {
        this.position = position;
    }

    @Override
    public StoreBaseEvent toStoreEvent() {
        return new StorePositionReleased(position.x(), position.y());
    }
}
