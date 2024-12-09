package CyDine;

import CyDine.FoodItems.FoodItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;    // SBv3

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ArjunSystemTest {


    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }
    @Test
    public void getAllFoodsTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/FoodItem");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONArray foodItems = new JSONArray(responseBody);
            assertNotNull(foodItems);
            assertTrue(foodItems.length() > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFoodsByIdTest() {
        int testId = 1; // Assuming a food item with ID 1 exists
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/FoodItem/" + testId);

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONObject foodItem = new JSONObject(responseBody);
            assertEquals(testId, foodItem.getInt("id"));
            assertNotNull(foodItem.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createFoodTest() {
        JSONObject foodItem = new JSONObject();
        try {
            foodItem.put("name", "apple");
            foodItem.put("protein", 10);
            foodItem.put("carbs", 20);
            foodItem.put("fat", 5);
            foodItem.put("calories", 170);
            foodItem.put("userId", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(foodItem.toString())
                .when()
                .post("/FoodItem");


        String responseBody = response.getBody().asString();
        assertNotNull(responseBody);
        assertTrue(Integer.parseInt(responseBody) > 0);
    }










    @Test
    public void getAllMealPlansTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/mealplans");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONArray mealPlans = new JSONArray(responseBody);
            assertNotNull(mealPlans);
            assertTrue(mealPlans.length() > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMealPlanByIdTest() {
        int testId = 1; // Assuming a meal plan with ID 1 exists
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/mealplans/" + testId);

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONObject mealPlan = new JSONObject(responseBody);
            assertEquals(testId, mealPlan.getInt("id"));
            assertNotNull(mealPlan.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createMealPlanTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/mealplans");


        String responseBody = response.getBody().asString();
        assertNotNull(responseBody);
        assertTrue(Integer.parseInt(responseBody) > 0);
    }





    @Test
    public void getAllDininghallFoodsTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/Dininghall");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONArray diningHalls = new JSONArray(responseBody);
            assertNotNull(diningHalls);
            assertTrue(diningHalls.length() > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDiningHallFoodsTest() {
        String diningHall = "ExampleHall";
        String time = "Lunch";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/Dininghall/" + diningHall + "/" + time);

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONArray foods = new JSONArray(responseBody);
            assertNotNull(foods);
            for (int i = 0; i < foods.length(); i++) {
                JSONObject food = foods.getJSONObject(i);
                assertEquals(diningHall, food.getString("dininghall"));
                assertEquals(time, food.getString("time"));
                assertEquals(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), food.getString("date").substring(0, 10));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllDiningHallFoodsTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/Dininghall/today");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONArray foods = new JSONArray(responseBody);
            assertNotNull(foods);
            for (int i = 0; i < foods.length(); i++) {
                JSONObject food = foods.getJSONObject(i);
                assertEquals(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), food.getString("date").substring(0, 10));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDininghallFoodsByIdTest() {
        int testId = 1;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/Dininghall/" + testId);

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = response.getBody().asString();
        try {
            JSONObject food = new JSONObject(responseBody);
            assertEquals(testId, food.getInt("id"));
            assertNotNull(food.getString("name"));
            assertNotNull(food.getString("dininghall"));
            assertNotNull(food.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
