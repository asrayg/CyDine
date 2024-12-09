package CyDine;

import CyDine.Weight.WeightEntry;
import CyDine.SleepTracking.SleepEntry;
import CyDine.Meditation.Meditation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AkhilSystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    // Weight Controller Tests

    @Test
    public void addWeightTest() {
        WeightEntry weightEntry = new WeightEntry();
        weightEntry.setWeight(70.5);
        weightEntry.setDate(LocalDate.now());

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(weightEntry)
                .when()
                .post("/weight/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"success\"}", response.getBody().asString());
    }

    @Test
    public void getUserWeightHistoryTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/weight/1");

        assertEquals(200, response.getStatusCode());
        try {
            JSONArray weightEntries = new JSONArray(response.getBody().asString());
            assertNotNull(weightEntries);
            assertTrue(weightEntries.length() > 0);
        } catch (JSONException e) {
            fail("Invalid JSON response");
        }
    }

    @Test
    public void getLatestWeightTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/weight/1/latest");

        assertEquals(200, response.getStatusCode());
        try {
            JSONObject weightEntry = new JSONObject(response.getBody().asString());
            assertNotNull(weightEntry.getDouble("weight"));
            assertNotNull(weightEntry.getString("date"));
        } catch (JSONException e) {
            fail("Invalid JSON response");
        }
    }

    // Sleep Controller Tests

    @Test
    public void addSleepTest() {
        SleepEntry sleepEntry = new SleepEntry();
        sleepEntry.setHoursSlept(7.5);
        sleepEntry.setDate(LocalDate.now());

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(sleepEntry)
                .when()
                .post("/sleep/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"success\"}", response.getBody().asString());
    }

    @Test
    public void getUserSleepHistoryTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/sleep/1");

        assertEquals(200, response.getStatusCode());
        try {
            JSONArray sleepEntries = new JSONArray(response.getBody().asString());
            assertNotNull(sleepEntries);
            assertTrue(sleepEntries.length() > 0);
        } catch (JSONException e) {
            fail("Invalid JSON response");
        }
    }

    @Test
    public void getLatestSleepTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/sleep/1/latest");

        assertEquals(200, response.getStatusCode());
        try {
            JSONObject sleepEntry = new JSONObject(response.getBody().asString());
            assertNotNull(sleepEntry.getDouble("hoursSlept"));
            assertNotNull(sleepEntry.getString("date"));
        } catch (JSONException e) {
            fail("Invalid JSON response");
        }
    }

    // Meditation Controller Tests

    @Test
    public void getAllMeditationsForUserTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/users/1/meditation");

        assertEquals(200, response.getStatusCode());
        try {
            JSONArray meditations = new JSONArray(response.getBody().asString());
            assertNotNull(meditations);
            assertTrue(meditations.length() > 0);
        } catch (JSONException e) {
            fail("Invalid JSON response");
        }
    }

    @Test
    public void getTodaysMeditationsForUserTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/users/1/meditation/today");

        assertEquals(200, response.getStatusCode());
        try {
            JSONArray meditations = new JSONArray(response.getBody().asString());
            assertNotNull(meditations);
            for (int i = 0; i < meditations.length(); i++) {
                JSONObject meditation = meditations.getJSONObject(i);
                assertEquals(LocalDate.now().toString(), meditation.getString("date").substring(0, 10));
            }
        } catch (JSONException e) {
            fail("Invalid JSON response");
        }
    }
}