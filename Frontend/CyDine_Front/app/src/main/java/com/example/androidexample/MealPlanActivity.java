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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MealPlanActivity extends AppCompatActivity {

    private static final String MEAL_PLAN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans";
    private static final String FOOD_ITEM_URL = "http://coms-3090-020.class.las.iastate.edu:8080/fooditems";  // Base URL for food items

    private LinearLayout mealPlanContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        Button addMealPlanButton = findViewById(R.id.add_meal_plan_button);
        mealPlanContainer = findViewById(R.id.meal_plan_container);

        // Fetch meal plans
        fetchMealPlans();

        addMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMealPlan();  // Call method to handle POST request for new meal plan
            }
        });
    }

    private void addNewMealPlan() {
        // Create a POST request to add a new meal plan
        StringRequest addMealPlanRequest = new StringRequest(
                Request.Method.POST,
                MEAL_PLAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("AddMealPlanResponse", response);
                        Toast.makeText(MealPlanActivity.this, "New Meal Plan added successfully!", Toast.LENGTH_SHORT).show();
                        // Refresh the meal plans to get the updated list
                        fetchMealPlans();
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
                // Build the request body for the new meal plan
                String requestBody = "{\"foods\":\"\","
                        + "\"protein\":0,"
                        + "\"carbs\":0,"
                        + "\"fat\":0,"
                        + "\"finalCalories\":0}";

                Log.d("AddMealPlanRequestBody", requestBody);  // Log the request body
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                // Set headers for the request (Content-Type: JSON)
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(addMealPlanRequest);
    }

    private void fetchMealPlans() {
        StringRequest mealPlanRequest = new StringRequest(
                Request.Method.GET,
                MEAL_PLAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Clear the current meal plan container
                            mealPlanContainer.removeAllViews();

                            // Parse the response JSON array of meal plans
                            JSONArray mealPlanArray = new JSONArray(response);

                            // Loop through each meal plan and add it to the UI
                            for (int i = 0; i < mealPlanArray.length(); i++) {
                                JSONObject mealPlan = mealPlanArray.getJSONObject(i);

                                // Extract meal plan details
                                int id = mealPlan.getInt("id");
                                String foods = mealPlan.getString("foods");  // Food item IDs as a string
                                int protein = mealPlan.getInt("protein");
                                int carbs = mealPlan.getInt("carbs");
                                int fat = mealPlan.getInt("fat");
                                int finalCalories = mealPlan.getInt("finalCalories");
                                String date = mealPlan.optString("date", "No date");

                                // Add the meal plan to the UI
                                addMealPlanToUI(id, foods, protein, carbs, fat, finalCalories, date);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MealPlanActivity.this, "Error parsing meal plan data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MealPlanError", error.toString());
                        Toast.makeText(MealPlanActivity.this, "Error fetching meal plans: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(mealPlanRequest);
    }

    private void fetchFoodItems(final String foodIDs, final int mealPlanId, final int protein, final int carbs, final int fat, final int finalCalories, final String date) {
        if (foodIDs == null || foodIDs.isEmpty()) {
            addMealPlanToUI(mealPlanId, "No Foods", protein, carbs, fat, finalCalories, date);
            return;
        }

        String[] foodIdArray = foodIDs.split(",");
        final StringBuilder foodNamesBuilder = new StringBuilder();
        final int totalItems = foodIdArray.length;
        int[] fetchedCount = {0};  // To keep track of how many food items we have fetched

        for (String foodIdStr : foodIdArray) {
            int foodId = Integer.parseInt(foodIdStr.trim());

            // Fetch each food item by its ID
            StringRequest foodItemRequest = new StringRequest(
                    Request.Method.GET,
                    FOOD_ITEM_URL + "/" + foodId,  // Get food item by ID
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // Parse the food item JSON object
                                JSONObject foodItem = new JSONObject(response);
                                String foodName = foodItem.getString("name");

                                // Append the food name to the StringBuilder
                                foodNamesBuilder.append(foodName).append(", ");
                                fetchedCount[0]++;  // Increment the fetched count

                                // Once all food items are fetched, update the UI
                                if (fetchedCount[0] == totalItems) {
                                    if (foodNamesBuilder.length() > 0 && foodNamesBuilder.toString().endsWith(", ")) {
                                        foodNamesBuilder.setLength(foodNamesBuilder.length() - 2);  // Remove trailing comma and space
                                    }

                                    // Add the meal plan to the UI with the fetched food names
                                    addMealPlanToUI(mealPlanId, foodNamesBuilder.toString(), protein, carbs, fat, finalCalories, date);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MealPlanActivity.this, "Error parsing food item data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("FoodItemError", error.toString());
                            Toast.makeText(MealPlanActivity.this, "Error fetching food item: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            // Add the request to the Volley request queue for each food item
            Volley.newRequestQueue(this).add(foodItemRequest);
        }
    }

    private void addMealPlanToUI(int id, String foods, int protein, int carbs, int fat, int finalCalories, String date) {
        // Inflate a new meal plan item layout
        View mealPlanView = LayoutInflater.from(this).inflate(R.layout.meal_plan_item, null);

        // Find and set the views in the layout
        TextView mealPlanHeader = mealPlanView.findViewById(R.id.meal_plan_header);
        EditText mealNameEditText = mealPlanView.findViewById(R.id.meal_name);
        EditText proteinValueEditText = mealPlanView.findViewById(R.id.protein_value);
        EditText fatValueEditText = mealPlanView.findViewById(R.id.fat_value);
        EditText carbsValueEditText = mealPlanView.findViewById(R.id.carbs_value);
        EditText caloriesValueEditText = mealPlanView.findViewById(R.id.calories_value);
        Button saveButton = mealPlanView.findViewById(R.id.save_button);
        Button deleteButton = mealPlanView.findViewById(R.id.delete_button);

        // Set meal plan details from API data
        mealPlanHeader.setText("Meal Plan " + id);
        mealNameEditText.setText(foods);
        proteinValueEditText.setText(String.valueOf(protein));
        fatValueEditText.setText(String.valueOf(fat));
        carbsValueEditText.setText(String.valueOf(carbs));
        caloriesValueEditText.setText(String.valueOf(finalCalories));

        // Set click listener for the save button
        saveButton.setOnClickListener(v -> {
            // Handle saving the meal plan
            updateMealPlan(id, mealNameEditText.getText().toString(),
                    Integer.parseInt(proteinValueEditText.getText().toString()),
                    Integer.parseInt(fatValueEditText.getText().toString()),
                    Integer.parseInt(carbsValueEditText.getText().toString()),
                    Integer.parseInt(caloriesValueEditText.getText().toString()));
        });

        // Set click listener for the delete button
        deleteButton.setOnClickListener(v -> {
            // Call method to delete the meal plan
            deleteMealPlan(id, mealPlanView);
        });

        // Add the meal plan view to the container
        mealPlanContainer.addView(mealPlanView);
    }

    private void updateMealPlan(int mealPlanId, String foods, int protein, int fat, int carbs, int finalCalories) {
        String updateUrl = MEAL_PLAN_URL + "/" + mealPlanId; // Construct the PUT URL

        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateMealPlanResponse", response);
                        Toast.makeText(MealPlanActivity.this, "Meal Plan updated successfully!", Toast.LENGTH_SHORT).show();
                        // Refresh the meal plans to get the updated list
                        fetchMealPlans();
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
                // Build the request body for the updated meal plan
                String requestBody = "{\"foods\":\"" + foods + "\","
                        + "\"protein\":" + protein + ","
                        + "\"carbs\":" + carbs + ","
                        + "\"fat\":" + fat + ","
                        + "\"finalCalories\":" + finalCalories + "}";

                Log.d("UpdateMealPlanRequestBody", requestBody); // Log the request body
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                // Set headers for the request (Content-Type: JSON)
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(updateRequest);
    }

    private void deleteMealPlan(final int mealPlanId, final View mealPlanView) {
        String deleteUrl = MEAL_PLAN_URL + "/" + mealPlanId;  // Construct delete URL with meal plan ID

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Success response
                        Log.d("DeleteMealPlan", "Meal Plan " + mealPlanId + " deleted successfully");
                        Toast.makeText(MealPlanActivity.this, "Meal Plan " + mealPlanId + " deleted!", Toast.LENGTH_SHORT).show();

                        // Remove the meal plan view from the container
                        mealPlanContainer.removeView(mealPlanView);

                        // Fetch updated meal plans to refresh the list
                        fetchMealPlans();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Enhanced error logging
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

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(deleteRequest);
    }
}
