package tnt.beam.data_model;

import java.io.Serializable;
import java.time.ZonedDateTime;

public record SensorMeasurement (
        ZonedDateTime eventTime,
        ZonedDateTime processTime,
        Integer sensorId,
        Double value
) implements Serializable {}
