package tnt.beam;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import tnt.beam.data_model.SensorMeasurement;

public class PrintMean  extends DoFn<KV<Integer, Double>, String> {
    @ProcessElement
    public void processElement(@Element KV<Integer, Double> input, OutputReceiver<String> output) {

        System.out.println("Sensor " + input.getKey() + " mean: " + input.getValue());
        output.output("lol");
    }
}
