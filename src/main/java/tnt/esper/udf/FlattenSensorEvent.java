package tnt.esper.udf;

import tnt.esper.entities.FlattenedSensorData;
import tnt.generators.Events.SensorDataRecord;


import java.util.ArrayList;
import java.util.List;

public class FlattenSensorEvent {

     public static List<FlattenedSensorData> flatten(SensorDataRecord sensorData) {
         List<FlattenedSensorData> result = new ArrayList<>();
         List<Double> values = sensorData.getValue();

         for (Double value : values) {
             result.add(new FlattenedSensorData(
                     sensorData.getSensorId(),
                     value,
                     0.0, // Placeholder for measurement
                     sensorData.getTimestamp().toEpochSecond() // Convert timestamp to seconds
             ));
         }
         return result;
     }
}
