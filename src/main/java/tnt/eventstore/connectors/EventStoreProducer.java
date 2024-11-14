package tnt.eventstore.connectors;

import tnt.cqrs_writer.framework.events.BaseEvent;

import jakarta.jms.JMSException;
import java.util.List;

public interface EventStoreProducer {
    /**
     * Speichert ein einzelnes Event in dem spezifischen Event-Scope.
     * @param events Die zu speichernden Events
     * @throws EventStoreException falls ein Speicherfehler auftritt
     */
    void storeEvent(List<BaseEvent> events) throws EventStoreException, JMSException;

}
