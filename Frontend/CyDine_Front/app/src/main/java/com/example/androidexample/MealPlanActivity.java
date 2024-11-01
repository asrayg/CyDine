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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MealPlanActivity extends AppCompatActivity {


    private Map<String, JSONObject> foodItemMap = new HashMap<>();
    private static final String MEAL_PLAN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans";
    private static final String FOOD_ITEM_URL = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem";

    private LinearLayout mealPlanContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        String userId = getIntent().getStringExtra("userId");

        // Print the userId in the log
        Log.d("MealPlanActivity", "User ID: " + userId);

        // Optional: Show the userId in a Toast message
        Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_LONG).show();

        Button addMealPlanButton = findViewById(R.id.add_meal_plan_button);
        mealPlanContainer = findViewById(R.id.meal_plan_container);


        // Fetch meal plans
        fetchMealPlans();
        fetchFoodItems();

        addMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMealPlan();
            }
        });
    }

    private void fetchFoodItems() {
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
                                String foodName = foodItem.getString("name").toLowerCase();
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

    private void addNewMealPlan() {
        // Create a POST request to add a new meal plan
        StringRequest addMealPlanRequest = new StringRequest(
                Request.Method.POST,
                MEAL_PLAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AddMealPlanResponse", response);
                        Toast.makeText(MealPlanActivity.this, "New Meal Plan added successfully!", Toast.LENGTH_SHORT).show();
                        fetchMealPlans(); // Refresh meal plans
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

    private void fetchMealPlans() {
        StringRequest mealPlanRequest = new StringRequest(
                Request.Method.GET,
                MEAL_PLAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mealPlanContainer.removeAllViews();
                            JSONArray mealPlanArray = new JSONArray(response);

                            // Initialize totals
                            int totalCalories = 0;
                            int totalProtein = 0;
                            int totalFat = 0;
                            int totalCarbs = 0;

                            for (int i = 0; i < mealPlanArray.length(); i++) {
                                JSONObject mealPlan = mealPlanArray.getJSONObject(i);
                                int id = mealPlan.getInt("id");
                                int protein = mealPlan.getInt("protein");
                                int carbs = mealPlan.getInt("carbs");
                                int fat = mealPlan.getInt("fat");
                                int finalCalories = mealPlan.getInt("finalCalories");
                                String date = mealPlan.optString("date", "No date");

                                // Accumulate the totals
                                totalProtein += protein;
                                totalCarbs += carbs;
                                totalFat += fat;
                                totalCalories += finalCalories;

                                // Handle food items array
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
                                addMealPlanToUI(id, foodNamesBuilder.toString(), protein, carbs, fat, finalCalories, date);
                            }

                            // Update total views with accumulated totals
                            TextView totalCaloriesView = findViewById(R.id.total_calories);
                            TextView totalProteinView = findViewById(R.id.total_protein);
                            TextView totalFatView = findViewById(R.id.total_fat);
                            TextView totalCarbsView = findViewById(R.id.total_carbs);

                            totalCaloriesView.setText("Total Calories: " + totalCalories);
                            totalProteinView.setText("Total Protein: " + totalProtein + "g");
                            totalFatView.setText("Total Fat: " + totalFat + "g");
                            totalCarbsView.setText("Total Carbs: " + totalCarbs + "g");

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

        Volley.newRequestQueue(this).add(mealPlanRequest);
    }


    private void addMealPlanToUI(int id, String foods, int protein, int carbs, int fat, int finalCalories, String date) {
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
        mealNameEditText.setText(foods);
        proteinValueEditText.setText(String.valueOf(protein));
        fatValueEditText.setText(String.valueOf(fat));
        carbsValueEditText.setText(String.valueOf(carbs));
        caloriesValueEditText.setText(String.valueOf(finalCalories));

        saveButton.setOnClickListener(v -> {
            String foodItemsText = mealNameEditText.getText().toString();
            // Convert the food items into a list
            List<String> foodItems = Arrays.asList(foodItemsText.split(","));


            // Reset the fields for recalculation
            int totalProtein = 0, totalFat = 0, totalCarbs = 0, totalCalories = 0;

            // Loop through the unique food items and retrieve their nutritional values
            for (String food : foodItems) {
                food = food.trim().toLowerCase(); // Normalize the food name
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
            updateMealPlan(id, mealNameEditText.getText().toString());
        });


        deleteButton.setOnClickListener(v -> {
            deleteMealPlan(id, mealPlanView);
        });

        mealPlanContainer.addView(mealPlanView);
    }

    private void updateMealPlan(int mealPlanId, String foods) {
        String updateUrl = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans/"+mealPlanId+"/fooditems/add/byName"; // Ensure you're hitting the correct URL with the meal plan ID

        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateMealPlanResponse", response);
                        Toast.makeText(MealPlanActivity.this, "Meal Plan updated successfully!", Toast.LENGTH_SHORT).show();
                        //fetchMealPlans(); // Refresh the meal plans to show the update
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
                // Create the JSON body with the fields that need to be updated
                // Ensure you're sending the correct fields that the server expects for an update
                try {
                    JSONObject requestBody = new JSONObject();
                    String[] foodItems = foods.split(",");
                    for(String food: foodItems){
                        requestBody.put("foods", food);
                        Log.d("UpdateMealPlanRequestBody", requestBody.toString());
                    }
                    return foods.getBytes();
                } catch (JSONException e) {
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


    private void deleteMealPlan(final int mealPlanId, final View mealPlanView) {
        String deleteUrl = MEAL_PLAN_URL + "/" + mealPlanId;

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DeleteMealPlan", "Meal Plan " + mealPlanId + " deleted successfully");
                        Toast.makeText(MealPlanActivity.this, "Meal Plan " + mealPlanId + " deleted!", Toast.LENGTH_SHORT).show();
                        mealPlanContainer.removeView(mealPlanView);
                        fetchMealPlans(); // Refresh meal plans
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
