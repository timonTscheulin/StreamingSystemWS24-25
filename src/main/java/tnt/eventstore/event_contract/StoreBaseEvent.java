package tnt.eventstore.event_contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import java.io.Serializable;

public abstract class StoreBaseEvent implements Serializable {
    private static Long eventIdCounter = 0L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public StoreBaseEvent() {
        StoreBaseEvent.eventIdCounter++;
    }

    public String getId() {return eventIdCounter.toString();}

    public abstract String getEventScope();

    public abstract DomainBaseEvent toDomainEvent();

    public abstract String getEventDomain();

    public abstract String getEventType();

    //public abstract com.google.protobuf.GeneratedMessageV3 toProtoBuf();

    public String toJson() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(this);
    }

    public static <T extends StoreBaseEvent> T fromJson(String json, Class<T> eventClass) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, eventClass);
    }
}
