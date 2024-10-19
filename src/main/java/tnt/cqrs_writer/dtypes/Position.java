package tnt.cqrs_writer.dtypes;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {
}