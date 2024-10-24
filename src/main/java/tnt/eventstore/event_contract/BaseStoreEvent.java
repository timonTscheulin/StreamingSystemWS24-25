package tnt.eventstore.event_contract;

import java.io.Serializable;

public abstract class BaseStoreEvent implements Serializable {
    public abstract String getId();
}
