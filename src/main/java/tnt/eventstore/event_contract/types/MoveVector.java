package tnt.eventstore.event_contract.types;

import java.io.Serializable;

public record MoveVector(int x, int y) implements Serializable {
}