package tnt.cqrs_reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CqrsReaderApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CqrsReaderApplication.class);
        app.setAdditionalProfiles("reader");
        app.run(args);
    }
}