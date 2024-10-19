package tnt.eventstore;

public class EventScope {
    private String scope;

    public EventScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}


/*
package eventStore;

import events.IEvent;

import java.util.Objects;

public class EventScope {
    private final Class<? extends IEvent> eventType;
    private final String id;

    public EventScope(Class<? extends IEvent> eventType, String id) {
        this.eventType = eventType;
        this.id = id;
    }

    public Class<? extends IEvent> getEventType() {
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

S*/