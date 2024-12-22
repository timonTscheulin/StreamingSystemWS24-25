package tnt.beam;

import org.apache.beam.sdk.transforms.DoFn;
import tnt.beam.data_model.SensorMeasurement;

public class PrintElement extends DoFn<SensorMeasurement, String>
{
    @ProcessElement
    public void processElement(@Element SensorMeasurement input, OutputReceiver<String> output) {

        System.out.println(input);
        output.output("lol");
    }
}

