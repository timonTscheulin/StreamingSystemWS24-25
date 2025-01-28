package tnt.esper.data_types.distributing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
