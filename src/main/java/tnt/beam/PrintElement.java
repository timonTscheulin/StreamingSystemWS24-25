package tnt.beam;

import org.apache.beam.sdk.transforms.DoFn;

public class PrintElement extends DoFn<String, String>
{
    @ProcessElement
    public void processElement(@Element String input, OutputReceiver<String> output) {
        System.out.println(input);
        output.output(input.toLowerCase());
    }
}

