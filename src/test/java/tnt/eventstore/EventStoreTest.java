package tnt.eventstore;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import tnt.eventstore.connectors.EventStoreException;
import tnt.eventstore.event_contract.StoreBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreVehicleCreated;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@EnableConfigurationProperties(EventStoreProperties.class)
public class EventStoreTest {

    private EventStore eventStore;

    @Before
    public void setup() {
        // EventStoreConfiguration initialisiert den EventStore basierend auf der Konfiguration.
        EventStoreProperties properties = new EventStoreProperties();
        properties.setType("kafka");
        properties.getKafka().setBootstrapServers("localhost:29092");

        EventStoreConfiguration configuration = new EventStoreConfiguration(properties);
        eventStore = configuration.createEventStore();
    }

    @Test
    public void testEventStoreWriteAndRead() throws EventStoreException {
        // Step 1: Erzeuge Events
        StoreVehicleCreated event1 = new StoreVehicleCreated("vehicle-1", 10, 20);
        StoreVehicleCreated event2 = new StoreVehicleCreated("vehicle-2", 30, 40);

        try {
            // Step 2: Schreibe Events in den EventStore
            eventStore.store(List.of(event1.toDomainEvent(), event2.toDomainEvent()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StoreBaseEvent> events = null;
        try {
            // Step 3: Lese Events aus dem EventStore
            events = eventStore.getAllEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Step 4: Assertions
        assertNotNull(events);
        assertEquals(2, events.size());

        StoreVehicleCreated retrievedEvent1 = (StoreVehicleCreated) events.get(0);
        StoreVehicleCreated retrievedEvent2 = (StoreVehicleCreated) events.get(1);

        assertEquals("vehicle-1", retrievedEvent1.getVehicleId());
        assertEquals(10, retrievedEvent1.getStartX());
        assertEquals(20, retrievedEvent1.getStartY());

        assertEquals("vehicle-2", retrievedEvent2.getVehicleId());
        assertEquals(30, retrievedEvent2.getStartX());
        assertEquals(40, retrievedEvent2.getStartY());
    }
}