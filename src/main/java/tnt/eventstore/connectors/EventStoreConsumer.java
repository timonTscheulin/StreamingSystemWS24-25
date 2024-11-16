package tnt.eventstore.connectors;

import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.List;

public interface EventStoreConsumer {
    /**
     * Ruft alle Events eines bestimmten Scopes ab.
     * @param scope Der Scope, dessen Events abgerufen werden sollen
     * @return Liste der Events im angegebenen Scope
     * @throws EventStoreException falls ein Fehler beim Abrufen auftritt
     */
    //List<StoreBaseEvent> fetchEventsByScope(EventScope scope) throws EventStoreException;

    /**
     * Ruft alle gespeicherten Events ab.
     * @return Liste aller gespeicherten Events
     * @throws EventStoreException falls ein Fehler beim Abrufen auftritt
     */
    List<StoreBaseEvent> getAllEvents() throws EventStoreException;

}
