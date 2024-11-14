package tnt.cqrs_reader.controllers;


import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnt.cqrs_reader.service.QueryInstance;
import tnt.cqrs_reader.dtos.VehicleDTO;
import tnt.cqrs_reader.dtypes.Position;

import java.util.List;

@RestController
@RequestMapping("/cqrs_api/vehicles")
public class QueryController {
    private final QueryInstance queryInstance;

    public QueryController() {
        this.queryInstance = new QueryInstance();
    }

    @PostConstruct
    public void init() {
        this.queryInstance.startProjectors();
    }

    @GetMapping("/{name}")
    public ResponseEntity<VehicleDTO> getVehicleByName(@PathVariable String name) {
        VehicleDTO vehicle = this.queryInstance.getVehicleByName(name);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return this.queryInstance.getVehicles();
    }

    @GetMapping("/by-position")
    public List<VehicleDTO> getVehiclesAtPosition(@RequestParam("x") int x, @RequestParam("y") int y) {
        Position position = new Position(x, y);
        return this.queryInstance.getVehiclesAtPosition(position);
    }
}