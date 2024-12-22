package tnt.beam;

import org.apache.beam.sdk.transforms.Combine;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
import tnt.beam.data_model.SensorMeasurement;

import java.io.Serializable;

public class MeanSensorSpeed extends Combine.CombineFn<SensorMeasurement, MeanSensorSpeed.Accum, Double> {

    public static class Accum implements Serializable {
        double sum = 0.0; // store the current sum
        int count = 0; // store the number of elements already processed
    }

    @Override
    public Accum createAccumulator() {
        return new Accum();
    }

    @Override
    public Accum addInput(MeanSensorSpeed.Accum mutableAccumulator, SensorMeasurement input) {
        mutableAccumulator.sum += input.value();
        mutableAccumulator.count++;

        return mutableAccumulator;
    }

    @Override
    public Accum mergeAccumulators(@UnknownKeyFor @NonNull @Initialized Iterable<MeanSensorSpeed.Accum> accumulators) {
        Accum merged = createAccumulator();
        for (Accum accum : accumulators) {
            merged.sum += accum.sum;
            merged.count += accum.count;
        }
        return merged;
    }

    @Override
    public Double extractOutput(MeanSensorSpeed.Accum accumulator) {
        return accumulator.sum / accumulator.count;
    }
}
