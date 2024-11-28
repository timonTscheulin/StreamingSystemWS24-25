package tnt;

import tnt.connectors.KafkaProducerConnector;
import tnt.generators.Events.SensorDataRecord;
import tnt.generators.SimpleDataGenerator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        KafkaProducerConnector producer = new KafkaProducerConnector("localhost:29092");
        SimpleDataGenerator generator = new SimpleDataGenerator(
                1000,
                3,
                30,
                900,
                1100,
                70,
                100);

        while(true) {
            List<SensorDataRecord> data = generator.tick();

            data.stream().forEach(System.out::println);
            System.out.println("===================================================");
            System.out.println("Store Events...");
            producer.storeEvents(data);
            System.out.println("... store successful!");
            System.out.println("===================================================");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {};
        }

    }
}
