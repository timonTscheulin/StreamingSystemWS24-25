package tnt.eventstore.connectors;

import kotlin.NotImplementedError;
import tnt.cqrs_writer.framework.events.BaseEvent;
import tnt.eventstore.EventScope;
import tnt.eventstore.event_contract.BaseStoreEvent;

import java.util.List;

public class KafkaStore implements EventStoreProducer {

    @Override
    public void storeEvent(List<BaseEvent> events) throws EventStoreException {
        throw new NotImplementedError();
    }

}
