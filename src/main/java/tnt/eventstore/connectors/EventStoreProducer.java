package tnt.eventstore.connectors;

import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import jakarta.jms.JMSException;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.List;

public interface EventStoreProducer {
    /**
     * Speichert ein einzelnes Event in dem spezifischen Event-Scope.
     * @param events Die zu speichernden Events
     * @throws EventStoreException falls ein Speicherfehler auftritt
     */
    void storeEvents(List<StoreBaseEvent> events) throws EventStoreException, JMSException;
}
