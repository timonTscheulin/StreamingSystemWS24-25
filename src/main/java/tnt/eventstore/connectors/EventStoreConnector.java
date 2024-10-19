package tnt.eventstore.connectors;

import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.BaseStoreEvent;

import java.util.List;

public interface EventStoreConnector {
    /**
     * Speichert ein einzelnes Event in dem spezifischen Event-Scope.
     * @param event Das zu speichernde Event
     * @param scope Der Scope, unter dem das Event gespeichert wird
     * @throws EventStoreException falls ein Speicherfehler auftritt
     */
    void storeEvent(BaseStoreEvent event, EventScope scope) throws EventStoreException;

    /**
     * Ruft alle Events eines bestimmten Scopes ab.
     * @param scope Der Scope, dessen Events abgerufen werden sollen
     * @return Liste der Events im angegebenen Scope
     * @throws EventStoreException falls ein Fehler beim Abrufen auftritt
     */
    List<BaseStoreEvent> fetchEventsByScope(EventScope scope) throws EventStoreException;

    /**
     * Ruft alle gespeicherten Events ab.
     * @return Liste aller gespeicherten Events
     * @throws EventStoreException falls ein Fehler beim Abrufen auftritt
     */
    List<BaseStoreEvent> getAllEvents() throws EventStoreException;

    /**
     * (Optional) Methode zur Verbindungseröffnung, z. B. beim Start einer neuen Connector-Instanz.
     * @throws EventStoreException falls ein Verbindungsfehler auftritt
     */
    void connect() throws EventStoreException;

    /**
     * (Optional) Methode zur Schließung von Verbindungen, z. B. beim Beenden der Anwendung.
     * @throws EventStoreException falls ein Fehler beim Schließen der Verbindung auftritt
     */
    void disconnect() throws EventStoreException;

    boolean isConnected();

    String type();
}
