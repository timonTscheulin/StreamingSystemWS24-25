package tnt.cqrs_writer.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tnt.cqrs_writer.api.SimpleCommandsApi;
import tnt.cqrs_writer.api.VehicleCommands;
import tnt.eventstore.EventStore;

@Configuration
public class CommandsApiConfiguration {
    private final EventStore eventStore;

    public CommandsApiConfiguration(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Bean
    public VehicleCommands vehicleCommands() {
        return new SimpleCommandsApi(eventStore);
    }
}
