package tnt.esper.data_types.landing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FlattenedSensorData {
    private long timestamp; // Epoch-Millis
    private Integer sensorId;
    private Double measurement;
    private int lane;
}
