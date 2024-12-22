package tnt.beam;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import tnt.beam.data_model.SensorMeasurement;

public class MeasurementKV extends DoFn<SensorMeasurement, KV<Integer, SensorMeasurement>> {
    @ProcessElement
    public void processElement(@Element SensorMeasurement element, OutputReceiver<KV<Integer, SensorMeasurement>> out) {
        out.output(KV.of(element.sensorId(), element));
    }
}
