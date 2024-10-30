package tnt.cqrs_writer.dtypes;

public record PositionDelta(int x, int y) {
    public boolean isZero() {
        return x == 0 && y == 0;
    }
}