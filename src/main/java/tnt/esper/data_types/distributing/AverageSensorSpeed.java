package tnt.esper.data_types.distributing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AverageSensorSpeed {
    private int sensorId;
    private long beginTimestamp;
    private long windowSize;
    private Double averageSpeed;

    @Override
    public String toString() {
        return beginTimestamp + ": Average Speed from Sensor " + sensorId + ": " + averageSpeed;
    }
}
