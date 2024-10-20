package tnt.eventstore.connectors;

import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.BaseStoreEvent;

import java.util.List;

public class KafkaStore implements EventStoreConnector {

    @Override
    public void storeEvent(BaseStoreEvent event) throws EventStoreException {

    }


    @Override
    public List<BaseStoreEvent> fetchEventsByScope(EventScope scope) throws EventStoreException {
        return List.of();
    }

    @Override
    public List<BaseStoreEvent> getAllEvents() throws EventStoreException {
        return List.of();
    }

    @Override
    public void connect() throws EventStoreException {

    }

    @Override
    public void disconnect() throws EventStoreException {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String type() {
        return "Kafka Connector";
    }

}
