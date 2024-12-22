package tnt.beam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.beam.model.pipeline.v1.RunnerApi;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;

import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.record.MemoryRecords;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;
import tnt.beam.data_model.SensorMeasurement;

import java.util.Map;



public class TestMain {


    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);


        PCollection<KafkaRecord<Long, String>> kafkaRecords = pipeline
                .apply(KafkaIO.<Long, String>read()
                        .withBootstrapServers("localhost:29092")
                        .withTopic("SensorData")
                        .withKeyDeserializer(LongDeserializer.class)
                        .withValueDeserializer(StringDeserializer.class));
        PCollection<SensorMeasurement> measurements =  kafkaRecords.apply(ParDo.of(new RecordDecoder()));
        PCollection<SensorMeasurement> filteredMeasurements = measurements.apply(Filter.by(new InvalidRecordFilter()));
        filteredMeasurements.apply(ParDo.of(new PrintElement()));
        PCollection<KV<Integer, SensorMeasurement>> measurementKV =  filteredMeasurements.apply(ParDo.of(new MeasurementKV()));
        PCollection<KV<Integer, SensorMeasurement>> boundedMeasurements = measurementKV.apply(Window.into(FixedWindows.of(Duration.standardSeconds(30))));
        PCollection<KV<Integer, Double>> means = boundedMeasurements.apply(Combine.perKey(new MeanSensorSpeed()));
        means.apply(ParDo.of(new PrintMean()));

        pipeline.run().waitUntilFinish();

    }
}
