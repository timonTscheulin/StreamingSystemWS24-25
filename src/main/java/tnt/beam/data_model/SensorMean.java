package tnt.beam.data_model;

import java.io.Serializable;

public record SensorMean(Integer sensorId, Double averageSpeed) implements Serializable {
}
