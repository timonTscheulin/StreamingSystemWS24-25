package tnt.eventstore.connectors;

import tnt.eventstore.event_contract.StoreBaseEvent;

import java.util.List;

public interface EventStoreConsumer {
    /**
     * Ruft alle Events eines bestimmten Scopes ab.
     * @param eventDomain Der Scope, dessen Events abgerufen werden sollen
     * @param domainOffset Event offset von dem aus gelesen werden soll
     * @return Liste der Events im angegebenen Scope
     * @throws EventStoreException falls ein Fehler beim Abrufen auftritt
     */
    List<StoreBaseEvent> getEventsFromOffset(String eventDomain, Long domainOffset) throws EventStoreException;

    /**
     * Ruft alle gespeicherten Events ab.
     * @return Liste aller gespeicherten Events
     * @throws EventStoreException falls ein Fehler beim Abrufen auftritt
     */
    List<StoreBaseEvent> getAllEvents(String domain) throws EventStoreException;

}
