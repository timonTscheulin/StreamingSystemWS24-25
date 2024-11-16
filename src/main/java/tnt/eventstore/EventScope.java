package tnt.eventstore;

import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.Objects;

public class EventScope {
    private final Class<? extends StoreBaseEvent> eventType;
    private final String id;

    public EventScope(Class<? extends StoreBaseEvent> eventType, String id) {
        this.eventType = eventType;
        this.id = id;
    }

    public Class<? extends StoreBaseEvent> getEventType() {
        return eventType;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EventScope that = (EventScope) obj;
        return eventType.equals(that.eventType) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, id);
    }
}