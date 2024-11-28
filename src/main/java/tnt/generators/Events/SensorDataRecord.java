package tnt.generators.Events;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record SensorDataRecord(ZonedDateTime timestamp, Integer sensorId, List<Double> values) {
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return formatter.format(timestamp) + " " + sensorId + " " + values.toString();
    }
}
