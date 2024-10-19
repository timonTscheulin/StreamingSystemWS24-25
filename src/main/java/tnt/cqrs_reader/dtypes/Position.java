package tnt.cqrs_reader.dtypes;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {
}