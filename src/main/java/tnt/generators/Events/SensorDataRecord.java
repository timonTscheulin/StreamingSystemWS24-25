package tnt.generators.Events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class SensorDataRecord {
    @Getter
    private ZonedDateTime timestamp;
    @Getter
    private Integer sensorId;
    @Getter
    private List<Double> value;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return formatter.format(timestamp) + " " + sensorId + " " + value.toString();
    }

}
