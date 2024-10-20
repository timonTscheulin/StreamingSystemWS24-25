package tnt.eventstore.event_contract.types;

import java.io.Serializable;

public record AbsolutPoint (int x, int y) implements Serializable {
}
