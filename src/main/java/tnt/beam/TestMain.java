package tnt.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.PCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMain {


    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);

        final List<String> cities = Arrays.asList("Furtwangen", "Hamburg", "VS-SAchwennignen");

        PCollection<String> pc = pipeline.apply(Create.of(cities)).setCoder(StringUtf8Coder.of());
        PCollection<String> filtered = pc.apply(Filter.by(new SerializableFunction<String, Boolean>() {
            @Override
            public Boolean apply(String input) {
                return input.equals("Furtwangen");
            }
        }));
        filtered.apply(ParDo.of(new PrintElement()));

        pipeline.run().waitUntilFinish();

    }
}
