package tnt.cqrs_reader.projectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_reader.query_repositories.VehiclePositionRepository;
import tnt.eventstore.connectors.ActiveMQConsumer;
import tnt.eventstore.connectors.EventStoreConsumer;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleCreated;
import tnt.eventstore.event_contract.vehicle.StoreVehicleNewPosition;
import tnt.eventstore.event_contract.vehicle.StoreVehicleRemoved;

import java.util.List;

public class VehicleByPositionProjector extends BaseProjector {

    private EventStoreConsumer store = new ActiveMQConsumer();
    private static final Logger log = LoggerFactory.getLogger(VehicleByPositionProjector.class);
    private VehiclePositionRepository repository;

    public VehicleByPositionProjector(VehiclePositionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void project() {
        try {
            List<StoreBaseEvent> events = store.getAllEvents();
            for (StoreBaseEvent e : events) {
                if (e instanceof StoreVehicleCreated createdEvent) {
                    log.info("StoreVehicleCreated: {}", createdEvent);
                    process(createdEvent);
                } else if (e instanceof StoreVehicleRemoved removedEvent) {
                    log.info("StoreVehicleRemoved: {}", removedEvent);
                    process(removedEvent);
                } else if (e instanceof StoreVehicleNewPosition newPositionEvent) {
                    log.info("StoreVehicleNewPosition x:{} y:{}", newPositionEvent.getX(), newPositionEvent.getY());
                    process(newPositionEvent);
                }else {
                    log.warn("Unknown event received of type: {}", e.getClass());
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void process(StoreVehicleCreated cmd) {
        repository.add(cmd.getVehicleId(), cmd.getX(), cmd.getY());
    }

    private void process(StoreVehicleRemoved cmd) {
        repository.remove(cmd.getVehicleId());
    }

    private void process(StoreVehicleNewPosition cmd) {
        repository.move(cmd.getVehicleId(), cmd.getX(), cmd.getY());
    }
}
