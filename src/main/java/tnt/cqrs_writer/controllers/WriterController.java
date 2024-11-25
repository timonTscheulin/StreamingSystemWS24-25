package tnt.cqrs_writer.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnt.cqrs_writer.api.VehicleCommands;
import tnt.cqrs_writer.dtypes.PositionDelta;
import tnt.cqrs_writer.dtypes.PositionPoint;

import javax.management.InstanceAlreadyExistsException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("cqrs_api/vehicles")
public class WriterController {
    private final VehicleCommands localApi;

    public WriterController(VehicleCommands localApi) {
        this.localApi = localApi;
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String id) {
        try {
            localApi.removeVehicle(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Element Found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<String> createVehicle(@PathVariable String id) {
        try {
            localApi.createVehicle(id, new PositionPoint(0,0));
        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Element already exists");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping("/move")
    public ResponseEntity<String> moveVehicle(@RequestBody @Valid MoveApiDTO body) {
        try {
            localApi.moveVehicle(body.id(), new PositionDelta(body.deltaX(), body.deltaY()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Element Found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        return ResponseEntity.ok(body.id());
    }
}
