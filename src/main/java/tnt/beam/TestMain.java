package tnt.beam;

import org.apache.beam.model.pipeline.v1.RunnerApi;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;

import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;


public class TestMain {


    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);


        PCollection<KafkaRecord<Long, String>> kafkaRecords = pipeline
                .apply(KafkaIO.<Long, String>read()
                        .withBootstrapServers("localhost:29092")
                        .withTopic("SensorData")
                        .withKeyDeserializer(LongDeserializer.class)
                        .withValueDeserializer(StringDeserializer.class));



        //final List<String> cities = Arrays.asList("Furtwangen", "Hamburg", "VS-Schwennignen");
        /*PCollection<String> pc = pipeline.apply(Create.of(cities)).setCoder(StringUtf8Coder.of());
        PCollection<String> filtered = pc.apply(Filter.by(new SerializableFunction<String, Boolean>() {
            @Override
            public Boolean apply(String input) {
                return input.equals("Furtwangen");
            }
        }));
        filtered.apply(ParDo.of(new PrintElement()));
        */
        kafkaRecords.apply(ParDo.of(new PrintElement()));
        pipeline.run().waitUntilFinish();

    }
}
