package tnt.e2e;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import tnt.cqrs_writer.controllers.MoveApiDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehicleE2ETests {

    private static final String WRITE_API_BASE_URL = "http://localhost:8082/cqrs_api/vehicles";
    private static final String READ_API_BASE_URL = "http://localhost:8083/cqrs_api/vehicles";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = WRITE_API_BASE_URL;
    }

    @Test
    @Order(1)
    @DisplayName("Test Vehicle Creation, Movement, and Deletion")
    public void testVehicleLifecycle() throws InterruptedException {
        String vehicleId = "vehicle1";

        // Step 1: Create Vehicle
        given()
                .post("/create/" + vehicleId)
                .then()
                .statusCode(200)
                .body(equalTo(vehicleId));

        // Wait for Eventual Consistency
        Thread.sleep(40);

        // Step 2: Move Vehicle
        given()
                .contentType("application/json")
                .body(new MoveApiDTO(vehicleId, 1, 1))
                .post("/move")
                .then()
                .statusCode(200)
                .body(equalTo(vehicleId));

        // Wait for Eventual Consistency
        Thread.sleep(40);

        // Step 3: Delete Vehicle
        given()
                .post("/delete/" + vehicleId)
                .then()
                .statusCode(200)
                .body(equalTo(vehicleId));

        Thread.sleep(40);

        // Validate deletion via Read API
        RestAssured.baseURI = READ_API_BASE_URL;
        given()
                .get("/" + vehicleId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(2)
    @DisplayName("Test Vehicle Deletion after Reaching Movement Limit")
    public void testVehicleDeletionAfterMovementLimit() throws InterruptedException {
        RestAssured.baseURI = WRITE_API_BASE_URL;
        String vehicleId = "vehicle2";

        // Step 1: Create Vehicle
        given()
                .post("/create/" + vehicleId)
                .then()
                .statusCode(200);

        // Step 2: Move Vehicle 20 times
        for (int i = 0; i < 20; i++) {
            given()
                    .contentType("application/json")
                    .body(new MoveApiDTO(vehicleId, 1, 0))
                    .post("/move")
                    .then()
                    .statusCode(200);
        }

        // Wait for Eventual Consistency
        Thread.sleep(40);

        // Step 3: Verify Deletion via Read API
        RestAssured.baseURI = READ_API_BASE_URL;
        given()
                .get("/" + vehicleId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    @DisplayName("Test Vehicle Deletion when Moving to Previously Visited Position")
    public void testVehicleDeletionOnRevisitingPosition() throws InterruptedException {
        RestAssured.baseURI = WRITE_API_BASE_URL;
        String vehicleId = "vehicle3";

        // Step 1: Create Vehicle
        given()
                .post("/create/" + vehicleId)
                .then()
                .statusCode(200);

        // Step 2: Move Vehicle to Position (1, 1)
        given()
                .contentType("application/json")
                .body(new MoveApiDTO(vehicleId, 1, 1))
                .post("/move")
                .then()
                .statusCode(200);

        // Step 3: Move Vehicle Back to Starting Position (0, 0)
        given()
                .contentType("application/json")
                .body(new MoveApiDTO(vehicleId, -1, -1))
                .post("/move")
                .then()
                .statusCode(200);

        // Wait for Eventual Consistency
        Thread.sleep(40);

        // Step 4: Verify Deletion via Read API
        RestAssured.baseURI = READ_API_BASE_URL;
        given()
                .get("/" + vehicleId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    @DisplayName("Test Collision Handling and Vehicle Deletion")
    public void testVehicleCollisionAndDeletion() throws InterruptedException {
        RestAssured.baseURI = WRITE_API_BASE_URL;
        String vehicle1Id = "vehicle4";
        String vehicle2Id = "vehicle5";

        // Step 1: Create Two Vehicles
        given()
                .post("/create/" + vehicle1Id)
                .then()
                .statusCode(200);
        // Step 2: Move Vehicle 1 to Position (1, 1)
        given()
                .contentType("application/json")
                .body(new MoveApiDTO(vehicle1Id, 1, 1))
                .post("/move")
                .then()
                .statusCode(200);

        given()
                .post("/create/" + vehicle2Id)
                .then()
                .statusCode(200);


        // Step 3: Move Vehicle 2 to the Same Position (1, 1)
        given()
                .contentType("application/json")
                .body(new MoveApiDTO(vehicle2Id, 1, 1))
                .post("/move")
                .then()
                .statusCode(200);

        // Wait for Eventual Consistency
        Thread.sleep(40);

        // Step 4: Verify that Vehicle 1 was Deleted
        RestAssured.baseURI = READ_API_BASE_URL;
        given()
                .get("/" + vehicle1Id)
                .then()
                .statusCode(404);

        // Step 5: Verify that Vehicle 2 is Present
        boolean success = false;
        for (int i = 0; i < 3; i++) {
            try {
                given()
                        .get("/" + vehicle2Id)
                        .then()
                        .statusCode(200);
                success = true;
                break; // Exit the retry loop if successful
            } catch (AssertionError e) {
                if (i < 2) { // Only wait if more retries are left
                    Thread.sleep(10);
                } else {
                    throw e; // Rethrow if the last retry fails
                }
            }
        }
        if (!success) {
            throw new AssertionError("Failed to delete Vehicle 2 after 3 attempts");
        }

        RestAssured.baseURI = WRITE_API_BASE_URL;
        given()
                .post("/delete/" + vehicle2Id)
                .then()
                .statusCode(200)
                .body(equalTo(vehicle2Id));
    }
}
