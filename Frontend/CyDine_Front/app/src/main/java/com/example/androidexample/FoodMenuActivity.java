package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FoodMenuActivity extends AppCompatActivity {

    private Spinner diningCenterSpinner;
    private Spinner mealTypeSpinner;
    private LinearLayout foodOptionsContainer;
    private LinearLayout breakfastContainer;
    private LinearLayout lunchContainer;
    private LinearLayout dinnerContainer;
    private LinearLayout lateNightContainer;
    private String userId;
    private Button save;

    private Map<String, FoodOption> selectedMeals; // Store selected meals by meal type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        userId = getIntent().getStringExtra("userId");

        diningCenterSpinner = findViewById(R.id.dining_center_spinner);
        mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        foodOptionsContainer = findViewById(R.id.food_options_container);
        save = findViewById(R.id.save_button);

        // Containers for each meal type in "Selected Meals" section
        breakfastContainer = findViewById(R.id.breakfast_container);
        lunchContainer = findViewById(R.id.lunch_container);
        dinnerContainer = findViewById(R.id.dinner_container);
        lateNightContainer = findViewById(R.id.late_night_container);

        selectedMeals = new HashMap<>(); // Initialize the map

        Button generateButton = findViewById(R.id.generate_button);
        generateButton.setOnClickListener(v -> generateFoodOptions());
    }

    private void generateFoodOptions() {
        foodOptionsContainer.removeAllViews(); // Clear previous views

        // Get selected dining center and meal type
        String diningHall = diningCenterSpinner.getSelectedItem().toString();
        String mealType = mealTypeSpinner.getSelectedItem().toString();

        // Map spinner values to API path values if necessary
        diningHall = mapDiningHallName(diningHall);

        // Construct the URL
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/Dininghall/" + diningHall + "/" + mealType;

        // Create a JSON request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseFoodItems(response, mealType);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FoodMenuActivity.this, "Error fetching food items.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void parseFoodItems(JSONArray foodItems, String mealType) {
        try {
            for (int i = 0; i < foodItems.length(); i++) {
                JSONObject foodObject = foodItems.getJSONObject(i);

                String foodName = foodObject.getString("name");
                int calories = foodObject.getInt("calories");

                // Create and display food cards with the API data
                CardView foodCard = createFoodCard(foodName, calories, mealType);
                foodOptionsContainer.addView(foodCard);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private CardView createFoodCard(String foodName, int calories, String mealType) {
        CardView cardView = new CardView(this);
        cardView.setCardElevation(8);
        cardView.setRadius(12);
        cardView.setContentPadding(16, 16, 16, 16);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView foodNameText = new TextView(this);
        foodNameText.setText("Food: " + foodName);
        foodNameText.setTextSize(18);

        TextView calorieText = new TextView(this);
        calorieText.setText("Calories: " + calories + " kcal");

        layout.addView(foodNameText);
        layout.addView(calorieText);

        cardView.addView(layout);

        // Set a click listener to select this food option for the meal type
        cardView.setOnClickListener(v -> selectMealOption(mealType, new FoodOption(foodName, calories)));

        return cardView;
    }

    private void selectMealOption(String mealType, FoodOption foodOption) {
        selectedMeals.put(mealType, foodOption); // Update selected meal for this meal type

        // Update the corresponding UI section
        updateSelectedMealUI(mealType);
    }

    private void updateSelectedMealUI(String mealType) {
        LinearLayout targetContainer;

        switch (mealType) {
            case "Breakfast":
                targetContainer = breakfastContainer;
                break;
            case "Lunch":
                targetContainer = lunchContainer;
                break;
            case "Dinner":
                targetContainer = dinnerContainer;
                break;
            case "Late Night":
                targetContainer = lateNightContainer;
                break;
            default:
                return; // Unknown meal type, do nothing
        }

        targetContainer.removeAllViews(); // Clear previous selection for this meal type

        FoodOption selectedOption = selectedMeals.get(mealType);
        if (selectedOption != null) {
            TextView mealTextView = new TextView(this);
            mealTextView.setText(selectedOption.toString());
            mealTextView.setTextSize(16);
            targetContainer.addView(mealTextView);
        }
    }

    private String mapDiningHallName(String diningHall) {
        switch (diningHall) {
            case "Seasons":
                return "seasons-marketplace-2-2";
            case "Friley":
                return "friley-windows-2-2";
            case "UDCC":
                return "union-drive-marketplace-2-2";
            default:
                return diningHall;
        }
    }

    private static class FoodOption {
        private String foodName;
        private int calories;

        public FoodOption(String foodName, int calories) {
            this.foodName = foodName;
            this.calories = calories;
        }

        @Override
        public String toString() {
            return foodName + " - " + calories + " kcal";
        }
    }
}
