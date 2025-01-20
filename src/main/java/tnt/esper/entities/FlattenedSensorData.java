package tnt.esper.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FlattenedSensorData {
    @Getter
    private int sensorId;
    @Getter
    private double val;
    @Getter
    private double measurement;
    @Getter
    private double timestamp;

    @Override
    public String toString() {
        return "FlattenedSensorData{sensorId=" + sensorId + ", val=" + val + "}";
    }
}

