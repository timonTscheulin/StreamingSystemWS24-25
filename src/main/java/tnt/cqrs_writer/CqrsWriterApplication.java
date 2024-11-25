package tnt.cqrs_writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = { "tnt.eventstore", "tnt.cqrs_writer"})
public class CqrsWriterApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CqrsWriterApplication.class);
        app.setAdditionalProfiles("writer");
        app.run(args);
    }
}
