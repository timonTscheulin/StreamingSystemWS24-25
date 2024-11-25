package tnt.eventstore.event_contract.position;

import lombok.Getter;
import tnt.eventstore.event_contract.StoreBaseEvent;

@Getter
public abstract class StoreBasePosition extends StoreBaseEvent {
    private int x_position;
    private int y_position;
    private String positionId;

    public StoreBasePosition(int x, int y) {
        super();
        this.x_position = x;
        this.y_position = y;
        positionId = x + "_" + y;
    }

    public String getEventScope() { return positionId; }

    public String getEventDomain() {
        return "Position";
    }

}
