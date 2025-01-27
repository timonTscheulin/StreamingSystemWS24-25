package tnt.esper.data_types.distributing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SpeedDecrease {
    private int sensorId;
    private double averageSpeedOne;
    private double averageSpeedTwo;
    private double averageSpeedThree;

    @Override
    public String toString() {
        return "SpeedDecrease [sensorId=" + sensorId +
                ", averageSpeedOne=" + averageSpeedOne +
                ", averageSpeedTwo=" + averageSpeedTwo +
                ", averageSpeedThree=" + averageSpeedThree + "]";
    }
}
