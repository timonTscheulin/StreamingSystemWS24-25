package tnt.eventstore.event_contract.position;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tnt.cqrs_writer.domain_model.events.position.PositionReleased;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StorePositionReleased extends StoreBasePosition {

    @JsonCreator
    public StorePositionReleased(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
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
