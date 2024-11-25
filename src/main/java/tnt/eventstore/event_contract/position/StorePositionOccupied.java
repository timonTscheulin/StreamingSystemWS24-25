package tnt.eventstore.event_contract.position;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import tnt.cqrs_writer.domain_model.events.position.PositionOccupied;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StorePositionOccupied extends StoreBasePosition {
    @Getter
    private final String occupationId;

    @JsonCreator
    public StorePositionOccupied(
            @JsonProperty("occupationId") String occupationId,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
        super(x, y);
        this.occupationId = occupationId;
    }

    @Override
    public DomainBaseEvent toDomainEvent() {
        return new PositionOccupied(occupationId, new AbsolutPosition(getX_position(), getY_position()));
    }

    @Override
    public String getEventType() {
        return "StorePositionOccupied";
    }
}
