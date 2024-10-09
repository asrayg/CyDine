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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MealPlanActivity extends AppCompatActivity {

    private static final String MEAL_PLAN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans";
    private static final String FOOD_ITEM_URL = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem";
    private LinearLayout mealPlanContainer;
    private Button addMealPlanButton;
    private int mealPlanCount = 1; // To keep track of the meal plan number
    private Map<Integer, String> foodItemsMap = new HashMap<>(); // Map to store food items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        mealPlanContainer = findViewById(R.id.meal_plan_container);
        addMealPlanButton = findViewById(R.id.add_meal_plan_button);

        // Fetch food items and meal plans when the activity is created
        fetchFoodItems();
        fetchMealPlans();

        // Set click listener on the button to add meal plan manually
        addMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMealPlan();
            }
        });
    }

    private void fetchFoodItems() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FOOD_ITEM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response for food items
                            JSONArray foodItemsArray = new JSONArray(response);
                            for (int i = 0; i < foodItemsArray.length(); i++) {
                                JSONObject foodItemObject = foodItemsArray.getJSONObject(i);
                                int id = foodItemObject.getInt("id");
                                String name = foodItemObject.getString("name");
                                foodItemsMap.put(id, name); // Store in the map
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
                        Toast.makeText(MealPlanActivity.this, "Error fetching food items", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Error: " + error.getMessage());
                    }
                });

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void fetchMealPlans() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MEAL_PLAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response for meal plans
                            JSONArray mealPlansArray = new JSONArray(response);
                            for (int i = 0; i < mealPlansArray.length(); i++) {
                                JSONObject mealPlanObject = mealPlansArray.getJSONObject(i);
                                String foods = mealPlanObject.getString("foods");
                                int protein = mealPlanObject.getInt("protein");
                                int carbs = mealPlanObject.getInt("carbs");
                                int finalCalories = mealPlanObject.getInt("finalCalories");
                                int fat = mealPlanObject.getInt("fat");
                                String date = mealPlanObject.optString("date", "No date available");

                                // Convert food IDs to names
                                String foodNames = convertFoodIdsToNames(foods);

                                // Create and display the meal plan
                                displayMealPlan(mealPlanObject.getInt("id"), foodNames, protein, carbs, finalCalories, fat, date);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MealPlanActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MealPlanActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Error: " + error.getMessage());
                    }
                });

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private String convertFoodIdsToNames(String foodIds) {
        StringBuilder foodNames = new StringBuilder();
        String[] ids = foodIds.split(",");

        for (String idStr : ids) {
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                String foodName = foodItemsMap.get(id); // Get food name from map
                if (foodName != null) {
                    foodNames.append(foodName).append(", "); // Append food name
                }
            }
        }

        // Remove trailing comma and space if present
        if (foodNames.length() > 0) {
            foodNames.setLength(foodNames.length() - 2);
        }
        return foodNames.toString();
    }

    private void displayMealPlan(int mealPlanId, String foods, int protein, int carbs, int calories, int fat, String date) {
        // Inflate the meal plan layout
        View mealPlanView = LayoutInflater.from(this).inflate(R.layout.meal_plan_item, mealPlanContainer, false);

        // Set the meal plan header
        TextView mealPlanHeader = mealPlanView.findViewById(R.id.meal_plan_header);
        mealPlanHeader.setText("Meal Plan " + mealPlanId);

        // Set the food items
        EditText mealNameEditText = mealPlanView.findViewById(R.id.meal_name);
        mealNameEditText.setText(foods.isEmpty() ? "No food items" : foods);

        // Set the nutritional values
        EditText proteinEditText = mealPlanView.findViewById(R.id.protein_value);
        proteinEditText.setText(String.valueOf(protein));

        EditText fatEditText = mealPlanView.findViewById(R.id.fat_value);
        fatEditText.setText(String.valueOf(fat));

        EditText carbsEditText = mealPlanView.findViewById(R.id.carbs_value);
        carbsEditText.setText(String.valueOf(carbs));

        EditText caloriesEditText = mealPlanView.findViewById(R.id.calories_value);
        caloriesEditText.setText(String.valueOf(calories));

        // Add the meal plan view to the container
        mealPlanContainer.addView(mealPlanView);
    }

    private void addMealPlan() {
        // You can implement adding a meal plan manually if needed
        Toast.makeText(this, "Adding a new meal plan manually is not implemented.", Toast.LENGTH_SHORT).show();
    }
}
