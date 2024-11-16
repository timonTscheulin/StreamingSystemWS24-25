package tnt.eventstore.connectors;

import kotlin.NotImplementedError;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import java.util.List;

public class KafkaStore implements EventStoreProducer {

    @Override
    public void storeEvent(List<DomainBaseEvent> events) throws EventStoreException {
        throw new NotImplementedError();
    }

}
