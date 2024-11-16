package tnt.cqrs_writer.framework.events;

import tnt.eventstore.event_contract.StoreBaseEvent;

public interface DomainBaseEvent {
    /**
     * Wandelt das Domain-Event in ein Event für den Event Store um.
     * Muss von den abgeleiteten Klassen implementiert werden.
     * @return Das Event für den Event Store
     */
    StoreBaseEvent toStoreEvent();
}
