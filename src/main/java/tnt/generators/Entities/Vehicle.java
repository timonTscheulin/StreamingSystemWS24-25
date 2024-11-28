package tnt.generators.Entities;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;

@Setter
@Getter
public class Vehicle {
    private int pos;
    private int lane;
    private double currentSpeedMS;
    private int oldPos;
    private ZonedDateTime lastUpdate;


    public Vehicle(int pos, int lane, double initialSpeedMS, ZonedDateTime startTime) {
        this.pos = pos;
        this.oldPos = 0;
        this.lane = lane;
        this.currentSpeedMS = initialSpeedMS;
        this.lastUpdate = startTime;
    }

    public void updatePos(ZonedDateTime currentTime) {
        // function defines behavior of each vehicle

        long secondsSinceLastUpdate = Duration.between(lastUpdate, currentTime).getSeconds();

        int positionDelta = (int)( secondsSinceLastUpdate * currentSpeedMS);

        this.oldPos = this.pos;
        this.pos = this.oldPos + positionDelta;
        this.lastUpdate = currentTime;
    }
}
