package tnt.cqrs_reader.projectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.cqrs_reader.query_repositories.VehicleRepository;
import tnt.eventstore.connectors.ActiveMQConsumer;
import tnt.eventstore.connectors.EventStoreConsumer;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleCreated;
import tnt.eventstore.event_contract.vehicle.StoreVehicleRemoved;

import java.util.List;

public class VehicleProjector extends BaseProjector {
    private static final Logger log = LoggerFactory.getLogger(VehicleProjector.class);
    private EventStoreConsumer store = new ActiveMQConsumer();
    private VehicleRepository repository;

    public VehicleProjector(VehicleRepository repository) {
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
                } else {
                    log.warn("Unknown event received of type: {}", e.getClass());
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void process(StoreVehicleCreated cmd) {
        // Logik für StoreVehicleCreated-Ereignisse
        log.debug("Processing StoreVehicleCreated event for vehicle: {}", cmd.getVehicleId());
        // Weitere Logik zur Verarbeitung und Speicherung des Ereignisses im Repository
        repository.createVehicle(cmd.getVehicleId(), cmd.getX(), cmd.getY());
    }

    private void process(StoreVehicleRemoved cmd) {
        // Logik für StoreVehicleRemoved-Ereignisse
        log.debug("Processing StoreVehicleRemoved event for vehicle: {}", cmd.getVehicleId());
        // Weitere Logik zur Verarbeitung und Entfernung des Ereignisses im Repository
        repository.deleteVehicle(cmd.getVehicleId());
    }
}