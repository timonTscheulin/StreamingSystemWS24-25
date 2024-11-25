package tnt.cqrs_writer.controllers;

import jakarta.validation.constraints.NotBlank;

public record MoveApiDTO(@NotBlank String id, int deltaX, int deltaY) {
}
