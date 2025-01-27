package tnt.esper.data_types.processing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PreprocessedSensorData {
    private long timestamp;
    private int sensorId;
    private double speed;
    private String unit;

    @Override
    public String toString() {
        return "CleanedData [timestamp=" + timestamp + ", sensorId=" + sensorId + ", speed=" + speed;
    }
}
