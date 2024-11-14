package tnt.cqrs_reader.controllers;


import org.springframework.web.bind.annotation.*;
import tnt.cqrs_reader.QueryInstance;
import tnt.cqrs_reader.dtos.VehicleDTO;
import tnt.cqrs_reader.dtypes.Position;

import java.util.List;

@RestController
@RequestMapping("/cqrs_api/vehicles")
public class QueryController {
    private final QueryInstance queryInstance;

    public QueryController() {
        this.queryInstance = new QueryInstance();
        this.queryInstance.startProjectors();
    }

    @GetMapping("/{name}")
    public VehicleDTO getVehicleByName(@PathVariable String name) {
        return this.queryInstance.getVehicleByName(name);
    }

    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return this.queryInstance.getVehicles();
    }

    @GetMapping("/position")
    public List<VehicleDTO> getVehiclesAtPosition(@RequestParam("x") int x, @RequestParam("y") int y) {
        Position position = new Position(x, y);
        return this.queryInstance.getVehiclesAtPosition(position);
    }

}