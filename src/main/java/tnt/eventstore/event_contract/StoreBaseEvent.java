package tnt.eventstore.event_contract;

import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import java.io.Serializable;

public abstract class StoreBaseEvent implements Serializable {
    private static Long eventIdCounter = 0L;

    public StoreBaseEvent() {
        StoreBaseEvent.eventIdCounter++;
    }

    public String getId() {return eventIdCounter.toString();}

    public abstract DomainBaseEvent toDomainEvent();
}
