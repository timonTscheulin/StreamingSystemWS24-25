package tnt.eventstore.connectors;

import kotlin.NotImplementedError;
import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.BaseStoreEvent;

import java.util.List;

public class KafkaStore implements EventStoreConnector {

    @Override
    public void storeEvent(BaseStoreEvent event) throws EventStoreException {
        throw new NotImplementedError();
    }


    @Override
    public List<BaseStoreEvent> fetchEventsByScope(EventScope scope) throws EventStoreException {
        throw new NotImplementedError();
    }

    @Override
    public List<BaseStoreEvent> getAllEvents() throws EventStoreException {
        throw new NotImplementedError();
    }

    @Override
    public void connect() throws EventStoreException {
        throw new NotImplementedError();
    }

    @Override
    public void disconnect() throws EventStoreException {
        throw new NotImplementedError();
    }

    @Override
    public boolean isConnected() {
        throw new NotImplementedError();
    }

    @Override
    public String type() {
        return "Kafka Connector";
    }

}
