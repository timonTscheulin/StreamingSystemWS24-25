package tnt.cqrs_writer.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class MoveApiDTO {
    @NotBlank
    String id;
    int deltaX;
    int deltaY;
}
