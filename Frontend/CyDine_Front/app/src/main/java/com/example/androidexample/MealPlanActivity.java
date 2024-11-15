package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for managing meal plans, including fetching, adding, updating, and deleting meal plans.
 */
public class MealPlanActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users/";
    private static final String MEAL_PLAN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans";
    private Map<String, JSONObject> foodItemMap = new HashMap<>();
    private String userId;

    private LinearLayout mealPlanContainer;
    private Button addMealPlanButton;


    /**
     * Called when the activity is created.
     * Initializes the view and sets up the necessary requests and event listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        userId = getIntent().getStringExtra("userId");
        mealPlanContainer = findViewById(R.id.meal_plan_container);
        addMealPlanButton = findViewById(R.id.add_meal_plan_button);

        fetchFoodItems();
        fetchUserMealPlans();

        addMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMealPlan();
            }
        });
    }

    /**
     * Fetches the list of food items from the server.
     * Populates the foodItemMap with the food names as keys and the JSON objects as values.
     */
    private void fetchFoodItems() {
        String FOOD_ITEM_URL = BASE_URL + userId + "/FoodItems";
        StringRequest foodItemRequest = new StringRequest(
                Request.Method.GET,
                FOOD_ITEM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray foodItemArray = new JSONArray(response);
                            for (int i = 0; i < foodItemArray.length(); i++) {
                                JSONObject foodItem = foodItemArray.getJSONObject(i);
                                String foodName = foodItem.getString("name");
                                foodItemMap.put(foodName, foodItem); // Store food item in the map
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MealPlanActivity.this, "Error parsing food items", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FoodItemError", error.toString());
                        Toast.makeText(MealPlanActivity.this, "Error fetching food items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(this).add(foodItemRequest);
    }

    /**
     * Adds a new meal plan by making a POST request to the server.
     * Sends a basic JSON request to create the meal plan and then associates it with the user.
     */
    private void addNewMealPlan() {
        // Create a POST request to add a new meal plan
        StringRequest addMealPlanRequest = new StringRequest(
                Request.Method.POST,
                MEAL_PLAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AddMealPlanResponse", response);
                        try {
                            // Parse the plain text response as an integer for mealPlanId
                            int mealPlanID = Integer.parseInt(response.trim());

                            // Show a toast message with the meal plan ID
                            Toast.makeText(MealPlanActivity.this, "New Meal Plan added successfully! ID: " + mealPlanID, Toast.LENGTH_SHORT).show();

                            // Make a PUT request to associate the meal plan with the user
                            associateMealPlanWithUser(mealPlanID);
                        } catch (NumberFormatException e) {
                            Log.e("ParseError", "Error parsing response as integer: " + e.getMessage());
                            Toast.makeText(MealPlanActivity.this, "Failed to parse response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AddMealPlanError", error.toString());
                        Toast.makeText(MealPlanActivity.this, "Error adding new meal plan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                String requestBody = "{\"foods\":[], \"protein\":0, \"carbs\":0, \"fat\":0, \"finalCalories\":0}";
                Log.d("AddMealPlanRequestBody", requestBody);
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(addMealPlanRequest);
    }

    /**
     * Makes a PUT request to associate a new meal plan with the user.
     * @param mealPlanID The ID of the meal plan to associate with the user.
     */
    private void associateMealPlanWithUser(int mealPlanID) {
        String url = BASE_URL + userId + "/mealplan/" + mealPlanID;

        StringRequest putRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AssociateMealPlanResponse", response);
                        Toast.makeText(MealPlanActivity.this, "Meal Plan associated with user successfully!", Toast.LENGTH_SHORT).show();
                        fetchUserMealPlans(); // Refresh meal plans to show the new one
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AssociateMealPlanError", error.toString());
                        Toast.makeText(MealPlanActivity.this, "Error associating meal plan with user: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(putRequest);
    }



    /**
     * Fetches the user's meal plans from the server.
     */
    private void fetchUserMealPlans() {
        String url = BASE_URL + userId + "/mealplans";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the response
                        displayMealPlans(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MealPlanActivity", "Error fetching meal plans: " + error.getMessage());
                        Toast.makeText(MealPlanActivity.this, "Failed to fetch meal plans", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    /**
     * Displays the fetched meal plans in the UI.
     * @param mealPlans The JSONArray containing the meal plans.
     */
    private void displayMealPlans(JSONArray mealPlans) {
        mealPlanContainer.removeAllViews(); // Clear previous views

        for (int i = 0; i < mealPlans.length(); i++) {
            try {
                JSONObject mealPlan = mealPlans.getJSONObject(i);
                int protein = mealPlan.getInt("protein");
                int carbs = mealPlan.getInt("carbs");
                int fat = mealPlan.getInt("fat");
                int calories = mealPlan.getInt("finalCalories");
                int id = mealPlan.getInt("id");

                JSONArray foodItemsArray = mealPlan.getJSONArray("foodItems");
                StringBuilder foodNamesBuilder = new StringBuilder();
                for (int j = 0; j < foodItemsArray.length(); j++) {
                    JSONObject foodItem = foodItemsArray.getJSONObject(j);
                    String foodName = foodItem.getString("name");
                    foodNamesBuilder.append(foodName).append(", ");
                }
                if (foodNamesBuilder.length() > 0 && foodNamesBuilder.toString().endsWith(", ")) {
                    foodNamesBuilder.setLength(foodNamesBuilder.length() - 2);
                }

                // Add meal plan to UI
                addMealItemView(id, foodNamesBuilder.toString(), protein, carbs, fat, calories);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Adds a new meal item to the meal plan display.
     * @param id The ID of the meal plan.
     * @param foodItemName A comma-separated list of food names in the meal plan.
     * @param protein The total protein value for the meal.
     * @param carbs The total carbs value for the meal.
     * @param fat The total fat value for the meal.
     * @param calories The total calorie value for the meal.
     */
    private void addMealItemView(int id, String foodItemName, int protein, int carbs, int fat, int calories) {
        View mealPlanView = LayoutInflater.from(this).inflate(R.layout.meal_plan_item, null);
        TextView mealPlanHeader = mealPlanView.findViewById(R.id.meal_plan_header);
        EditText mealNameEditText = mealPlanView.findViewById(R.id.meal_name);
        EditText proteinValueEditText = mealPlanView.findViewById(R.id.protein_value);
        EditText fatValueEditText = mealPlanView.findViewById(R.id.fat_value);
        EditText carbsValueEditText = mealPlanView.findViewById(R.id.carbs_value);
        EditText caloriesValueEditText = mealPlanView.findViewById(R.id.calories_value);
        Button saveButton = mealPlanView.findViewById(R.id.save_button);
        Button deleteButton = mealPlanView.findViewById(R.id.delete_button);

        mealPlanHeader.setText("Food Item " + id);
        mealNameEditText.setText(foodItemName);
        proteinValueEditText.setText(String.valueOf(protein));
        fatValueEditText.setText(String.valueOf(fat));
        carbsValueEditText.setText(String.valueOf(carbs));
        caloriesValueEditText.setText(String.valueOf(calories));

        saveButton.setOnClickListener(v -> {
            String foodItemsText = mealNameEditText.getText().toString();
            // Convert the food items into a list
            List<String> foodItems = Arrays.asList(foodItemsText.split(","));
            Toast.makeText(MealPlanActivity.this, "Errajskdjkasdh " + foodItemsText , Toast.LENGTH_SHORT).show();


            // Reset the fields for recalculation
            int totalProtein = 0, totalFat = 0, totalCarbs = 0, totalCalories = 0;

            // Loop through the unique food items and retrieve their nutritional values
            for (String food : foodItems) {
                food = food.trim(); // Normalize the food name
                JSONObject foodItem = foodItemMap.get(food);
                if (foodItem != null) {
                    try {
                        totalProtein += foodItem.getInt("protein");
                        totalFat += foodItem.getInt("fat");
                        totalCarbs += foodItem.getInt("carbs");
                        totalCalories += foodItem.getInt("calories");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MealPlanActivity.this, "Unknown food item: " + food, Toast.LENGTH_SHORT).show();
                }
            }

            mealNameEditText.setText(String.join(", ", foodItems));
            proteinValueEditText.setText(String.valueOf(totalProtein));
            fatValueEditText.setText(String.valueOf(totalFat));
            carbsValueEditText.setText(String.valueOf(totalCarbs));
            caloriesValueEditText.setText(String.valueOf(totalCalories));

            // Now pass the updated values to updateMealPlan
            updateMealPlanWithFoodItem(id, mealNameEditText.getText().toString());
        });

        deleteButton.setOnClickListener(v -> {
            deleteMealPlan(id, mealPlanView);
        });



        mealPlanContainer.addView(mealPlanView);
    }


    /**
     * Updates the meal plan with a new food item.
     * @param id The ID of the meal plan to update.
     * @param foodItemName A comma-separated list of food items to add to the meal plan.
     */
    private void updateMealPlanWithFoodItem(int id, String foodItemName) {
        String updateUrl = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans/" + id + "/fooditems/add/byName/" + userId;

        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateMealPlanResponse", response);
                        Toast.makeText(MealPlanActivity.this, "Meal Plan updated successfully!", Toast.LENGTH_SHORT).show();
                        // fetchMealPlans(); // Refresh the meal plans to show the update
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateMealPlanError", error.toString());
                        Toast.makeText(MealPlanActivity.this, "Error updating meal plan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                try {
                    // Create JSON body with an array of foods
                    JSONObject requestBody = new JSONObject();
                    JSONArray foodsArray = new JSONArray();

                    // Split food items and add each to the array
                    String[] foodItems = foodItemName.split(",");
                    for (String food : foodItems) {
                        foodsArray.put(food.trim());
                    }

                    // Attach the foods array to the request body
                    requestBody.put("foods", foodsArray);

                    // Log the final request body for debugging
                    Log.d("UpdateMealPlanRequestBody", requestBody.toString());

                    // Return the request body as bytes
                    return requestBody.toString().getBytes("UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(updateRequest);
    }

    /**
     * Deletes a meal plan.
     * @param mealPlanId The ID of the meal plan to delete.
     * @param mealPlanView The view associated with the meal plan that will be removed from the UI.
     */
    private void deleteMealPlan(int mealPlanId, final View mealPlanView) {
        String deleteUrl = BASE_URL + userId + "/mealplan/" + mealPlanId;

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DeleteMealPlan", "Meal Plan " + mealPlanId + " deleted successfully");
                        Toast.makeText(MealPlanActivity.this, "Meal Plan " + mealPlanId + " deleted!", Toast.LENGTH_SHORT).show();
                        mealPlanContainer.removeView(mealPlanView);
                        fetchUserMealPlans(); // Refresh meal plans
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Unknown error occurred";
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseBody = new String(error.networkResponse.data);
                            errorMessage = "Error " + statusCode + ": " + responseBody;
                        }
                        Log.e("DeleteMealPlanError", errorMessage);
                        Toast.makeText(MealPlanActivity.this, "Error deleting meal plan: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(deleteRequest);
    }

}
