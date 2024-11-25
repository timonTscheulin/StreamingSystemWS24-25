package tnt.cqrs_reader.configurations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tnt.cqrs_reader.projectors.VehicleByPositionProjector;
import tnt.cqrs_reader.projectors.VehicleProjector;
import tnt.cqrs_reader.query_repositories.VehiclePositionRepository;
import tnt.cqrs_reader.query_repositories.VehicleRepository;
import tnt.cqrs_reader.service.QueryInstance;

import tnt.eventstore.EventStore;

@Configuration
public class QueryConfiguration {
    @Bean
    public VehicleRepository vehicleRepository() {
        return new VehicleRepository();
    }

    @Bean
    public VehiclePositionRepository vehiclePositionRepository() {
        return new VehiclePositionRepository();
    }

    @Bean
    public VehicleProjector vehicleProjector(
            VehicleRepository vehicleRepository,
            @Qualifier("vehicleEventStore") EventStore vehicleEventStore) {
        return new VehicleProjector(vehicleRepository, vehicleEventStore);
    }

    @Bean
    public VehicleByPositionProjector vehicleByPositionProjector(
            VehiclePositionRepository vehiclePositionRepository,
            @Qualifier("vehiclePositionEventStore") EventStore vehiclePositionEventStore) {
        return new VehicleByPositionProjector(vehiclePositionRepository, vehiclePositionEventStore);
    }

    @Bean
    public QueryInstance queryInstance(
            VehicleRepository vehicleRepository,
            VehiclePositionRepository vehiclePositionRepository,
            VehicleProjector vehicleProjector,
            VehicleByPositionProjector vehicleByPositionProjector) {
        return new QueryInstance(vehicleRepository, vehiclePositionRepository, vehicleProjector, vehicleByPositionProjector);
    }
}
