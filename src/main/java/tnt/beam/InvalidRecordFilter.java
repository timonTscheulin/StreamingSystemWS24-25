package tnt.beam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.transforms.SerializableFunction;
import tnt.beam.data_model.SensorMeasurement;

import java.util.Map;

public class InvalidRecordFilter implements SerializableFunction<SensorMeasurement, Boolean> {
    @Override
    public Boolean apply(SensorMeasurement input) {
        return input.value() > 0;
    }
}

