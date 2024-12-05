package tnt.beam;

import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

public class PrintElement extends DoFn<KafkaRecord<Long, String>, String>
{
    @ProcessElement
    public void processElement(@Element KafkaRecord<Long, String> input, OutputReceiver<String> output) {
        System.out.println(input.getKV().getValue());
        output.output("lol");
    }
}

