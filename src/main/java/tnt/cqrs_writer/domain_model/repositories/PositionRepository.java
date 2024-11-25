package tnt.cqrs_writer.domain_model.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import tnt.cqrs_writer.domain_model.aggregates.Position;
import tnt.cqrs_writer.domain_model.events.position.PositionOccupied;
import tnt.cqrs_writer.domain_model.events.position.PositionReleased;
import tnt.cqrs_writer.domain_model.value_objects.AbsolutPosition;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.EventStore;
import tnt.eventstore.connectors.InMemoryEventStore;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PositionRepository {

    private static final Logger log = LoggerFactory.getLogger(PositionRepository.class);
    private EventStore eventStore;

    public PositionRepository(EventStore eventStore){
        eventStore = eventStore;
    }


    public Position getPosition(AbsolutPosition targetPosition) {
        Position position = new Position(targetPosition);

        List<DomainBaseEvent> events = new ArrayList<>();

        try {
            events = eventStore.getAllEvents();
        } catch (Exception e) {
            log.error("Unable to load events.");
            log.error(e.getMessage());
        }

        for (DomainBaseEvent event : events) {
            if (event instanceof PositionOccupied positionOccupied) {
                if (positionOccupied.getPosition().equals(targetPosition)) {
                    position.replay(positionOccupied);
                }
            } else if (event instanceof PositionReleased positionReleased) {
                if (positionReleased.getPosition().equals(targetPosition)) {
                    position.replay(positionReleased);
                }
            }
        }

        return position;
    }
}
