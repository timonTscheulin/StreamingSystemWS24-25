package tnt.generators;

import org.apache.commons.collections.ArrayStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tnt.generators.Entities.Vehicle;
import tnt.generators.Events.SensorDataRecord;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimpleDataGenerator {
    private static final Logger log = LoggerFactory.getLogger(SimpleDataGenerator.class);
    private Integer streetLengthInM;
    private Integer numberOfLanes;
    private Integer numberOfSensors;
    private Integer minSpeedInKmH;
    private Integer maxSpeedInKmH;
    private Integer minMeasurementOffsetInMs;
    private Integer maxMeasurementOffsetInMs;
    private Integer minVehicleDistance;
    private List<Integer> sensorPositions;
    private List<Vehicle> vehicleList = new ArrayList<>();
    private final int minVehiclePerLane;
    private final int maxVehiclePerLane;
    private final List<Integer> startPositions;
    private final Random random = new Random();
    private int jam_position = -1;
    // global setting
    private final double jam_speed = 3; // ~ 2 km/h
    private int vehicle_left_counter = 0;


    public SimpleDataGenerator(
            Integer streetLengthInM,
            Integer numberOfLanes,
            Integer numberOfSensors,
            Integer minMeasurementOffsetInMs,
            Integer maxMeasurementOffsetInMs,
            Integer minSpeedInKmH,
            Integer maxSpeedInKmH
    ) {
        this.streetLengthInM = streetLengthInM;
        this.numberOfLanes = numberOfLanes;
        this.numberOfSensors = numberOfSensors;
        this.minMeasurementOffsetInMs = minMeasurementOffsetInMs;
        this.maxMeasurementOffsetInMs = maxMeasurementOffsetInMs;
        this.minSpeedInKmH = minSpeedInKmH;
        this.maxSpeedInKmH = maxSpeedInKmH;
        this.startPositions = new ArrayList<>(Collections.nCopies(numberOfLanes, 250));


        this.minVehicleDistance = this.maxSpeedInKmH / 2; // you should have a min distance of half of your tacho
        this.maxVehiclePerLane = (streetLengthInM / minVehicleDistance);
        this.minVehiclePerLane = 0;

        this.sensorPositions = new ArrayList<Integer>();

        double sensorDistance = (double)streetLengthInM / (numberOfSensors + 1);

        for (double i = 1; i < numberOfSensors + 1; i++) {
            sensorPositions.add((int)(i * sensorDistance));
        }

        log.info("Number of sensors: " + numberOfSensors);
        log.info("Sensor positions are:" + sensorPositions);
    }

    private void createNewVehicle(ZonedDateTime creationTime) {
        for (int i = 0; i < numberOfLanes; i++) {
            // if state is -1 => there is an vehicle
            // if state is 0 => the maximum probability of 50:50 has reached
            // if state is > 0 => probability is lower than 50 % and increased in the next run
            int state = startPositions.get(i);

            if (state == -1) {
                continue;
            }

            int guess = random.nextInt(1000);

            if(guess >= state) {
                // multiply by 10 to get one decimal number
                double initSpeed = ((double)getRandomInt(convertKmHToMs(minSpeedInKmH) * 10, convertKmHToMs(maxSpeedInKmH) * 10)) / 10;
                startPositions.set(i, -1);
                vehicleList.add(new Vehicle(0, i, initSpeed, creationTime));
            }
            else {
                startPositions.set(i, state / 2);
            }
        }
    }

    private void updatePositions(ZonedDateTime updateTime) {
        for (Vehicle vehicle : vehicleList) {
            vehicle.updatePos(updateTime);
            int oldPos = vehicle.getOldPos();
            int newPos = vehicle.getPos();

            // check vehicle has left start position
            if (oldPos <= minVehicleDistance && newPos >= minVehicleDistance ) {
                startPositions.set(vehicle.getLane(), 1000);
            }
        }
    }

    private void check_vehicle_has_reached_jam() {
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getPos() > this.jam_position && this.jam_position != -1) {
                vehicle.setCurrentSpeedMS(jam_speed);
            }
        }
    }

    private SensorDataRecord checkSensorPassages(ZonedDateTime measurementTime, int sensorId) {
        List<Double> measurements = new ArrayList<>();
        int sensorPosition = sensorPositions.get(sensorId);

        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getOldPos() <= sensorPosition && sensorPosition <= vehicle.getPos()) {
                boolean isRealMeasurement = getRandomInt(0, 100) != 0;
                if (isRealMeasurement) {
                    measurements.add(vehicle.getCurrentSpeedMS());
                }
                else {
                    measurements.add(vehicle.getCurrentSpeedMS() * -1);
                }
            }
        }
        return new SensorDataRecord(measurementTime ,sensorId, measurements);
    }

    private void checkVehicleLeftStreet() {
        //vehicleList.removeIf(vehicle -> vehicle.getPos() > this.streetLengthInM);
        List<Vehicle> vehiclesToRemove = new ArrayList<>();

        for (Vehicle vehicle : vehicleList) {
            if( vehicle.getPos() > this.streetLengthInM) {
                vehiclesToRemove.add(vehicle);
                vehicle_left_counter++;
            }
        }
        vehicleList.removeAll(vehiclesToRemove);

    }

    public int getVehicleLeftCounter() {
        return vehicle_left_counter;
    }

    private List<SensorDataRecord> generateData( ZonedDateTime measurementTime) {
        List<SensorDataRecord> sensorData = new ArrayList<>();

        for (int sensorPosition = 0; sensorPosition < sensorPositions.size(); sensorPosition++) {
            int measurementOffsetMs = getRandomInt(minMeasurementOffsetInMs, maxMeasurementOffsetInMs);
            ZonedDateTime sensorMeasurementTime = measurementTime.plus(Duration.ofMillis(measurementOffsetMs));
            sensorData.add(checkSensorPassages(sensorMeasurementTime, sensorPosition));
        }
        return sensorData;
    }

    public List<SensorDataRecord> tick() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        updatePositions(currentTime);
        createNewVehicle(currentTime);
        List<SensorDataRecord> sensorData = generateData(currentTime);
        checkVehicleLeftStreet();
        check_vehicle_has_reached_jam();
        return sensorData;
    }

    public void set_jam_at(int position) {
        if (position > 0 && position < this.streetLengthInM) {
            jam_position = position;
        }
    }

    public void solve_jam() {
        jam_position = -1;
    }

    private int convertKmHToMs(int speedKmH) {
        return speedKmH * 1000 / 3600;
    }

    private int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
