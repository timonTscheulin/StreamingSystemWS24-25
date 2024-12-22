package tnt.beam;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Partition;
import com.fasterxml.jackson.databind.ObjectMapper;
import tnt.beam.data_model.SensorMeasurement;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordDecoder extends DoFn<KafkaRecord<Long, String>, SensorMeasurement> {
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @ProcessElement
    public void processElement(@Element KafkaRecord<Long, String> in, OutputReceiver<SensorMeasurement> out) {
        String payload = in.getKV().getValue();
        try {
            Map<String, Object> record = mapper.readValue(payload, Map.class);
            ZonedDateTime processTime = ZonedDateTime.now();
            ZonedDateTime eventTime = ZonedDateTime.parse((String)record.getOrDefault("timestamp", ""), DateTimeFormatter.ISO_ZONED_DATE_TIME);
            Integer sensorId = (Integer)record.getOrDefault("sensorId", -1);
            List<Double> values = (List<Double>) record.get("values");

            for (Double value : values) {
                out.output(new SensorMeasurement(eventTime, processTime, sensorId, value));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
