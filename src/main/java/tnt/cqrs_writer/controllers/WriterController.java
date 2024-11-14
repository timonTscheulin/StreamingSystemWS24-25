package tnt.cqrs_writer.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tnt.cqrs_writer.api.SimpleCommandsApi;

@RestController
@RequestMapping("cqrs_api/vehicles")
public class WriterController {
    private final SimpleCommandsApi localApi;

    public WriterController() {
        this.localApi = new SimpleCommandsApi();
    }

    @PostMapping("/delete/{id}")
    public void deleteVehicle(@PathVariable String id) {

    }

}
