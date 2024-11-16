package tnt.eventstore.event_contract.position;

import lombok.Getter;
import tnt.eventstore.event_contract.StoreBaseEvent;

public abstract class StoreBasePosition extends StoreBaseEvent {
    @Getter
    private int x_position;
    @Getter
    private int y_position;
    @Getter
    private String positionId;

    public StoreBasePosition(int x, int y) {
        super();
        this.x_position = x;
        this.y_position = y;
        positionId = x + "_" + y;
    }

}
