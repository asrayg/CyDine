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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FoodMenuActivity extends AppCompatActivity {

    private Spinner diningCenterSpinner;
    private Spinner mealTypeSpinner;
    private LinearLayout foodOptionsContainer;
    private LinearLayout breakfastContainer;
    private LinearLayout lunchContainer;
    private LinearLayout dinnerContainer;

    private Map<String, FoodOption> selectedMeals; // Store selected meals by meal type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        diningCenterSpinner = findViewById(R.id.dining_center_spinner);
        mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        foodOptionsContainer = findViewById(R.id.food_options_container);
        breakfastContainer = findViewById(R.id.breakfast_container);
        lunchContainer = findViewById(R.id.lunch_container);
        dinnerContainer = findViewById(R.id.dinner_container);
        Button generateButton = findViewById(R.id.generate_button);
        selectedMeals = new HashMap<>(); // Initialize the map

        generateButton.setOnClickListener(v -> generateFoodOptions());
    }

    private void generateFoodOptions() {
        // Clear any existing views
        foodOptionsContainer.removeAllViews();

        // Generate 3 random food cards
        for (int i = 0; i < 3; i++) {
            String foodName = generateRandomFoodName();
            int calories = generateRandomCalories();
            int amount = generateRandomAmount();

            // Create a new card for each food option
            CardView foodCard = createFoodCard(foodName, calories, amount);
            foodOptionsContainer.addView(foodCard);
        }
    }

    private CardView createFoodCard(String foodName, int calories, int amount) {
        // Create a new CardView
        CardView cardView = new CardView(this);
        cardView.setCardElevation(8);
        cardView.setRadius(12);
        cardView.setContentPadding(16, 16, 16, 16);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add content to CardView
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView foodNameText = new TextView(this);
        foodNameText.setText("Food: " + foodName);
        foodNameText.setTextSize(18);

        TextView calorieText = new TextView(this);
        calorieText.setText("Calories: " + calories + " kcal");

        TextView amountText = new TextView(this);
        amountText.setText("Amount: " + amount + "g");

        layout.addView(foodNameText);
        layout.addView(calorieText);
        layout.addView(amountText);

        // Make card clickable to select the meal
        cardView.setOnClickListener(v -> selectFoodOption(foodName, calories, amount, cardView));

        cardView.addView(layout);
        return cardView;
    }

    private void selectFoodOption(String foodName, int calories, int amount, CardView cardView) {
        String mealType = mealTypeSpinner.getSelectedItem().toString(); // Get selected meal type

        // Remove previously selected meal if it exists
        FoodOption existingMeal = selectedMeals.get(mealType);
        if (existingMeal != null) {
            // Remove existing meal display
            removeMealDisplay(existingMeal, mealType);
        }

        FoodOption foodOption = new FoodOption(foodName, calories, amount);
        selectedMeals.put(mealType, foodOption); // Store the selected meal

        // Update selected meals display
        displaySelectedMeals(mealType);

        // Disable card after selection to prevent re-selection
        cardView.setClickable(false);
        cardView.setAlpha(0.5f); // Make it appear disabled
    }

    private void displaySelectedMeals(String mealType) {
        LinearLayout selectedContainer = getMealContainer(mealType);
        selectedContainer.removeAllViews();

        FoodOption meal = selectedMeals.get(mealType);
        if (meal != null) {
            // Create a TextView for the selected meal
            TextView mealText = new TextView(this);
            mealText.setText(meal.toString());
            mealText.setTextSize(16);
            mealText.setOnClickListener(v -> removeMeal(mealType));

            selectedContainer.addView(mealText);
        }
    }

    private LinearLayout getMealContainer(String mealType) {
        switch (mealType) {
            case "Breakfast":
                return breakfastContainer;
            case "Lunch":
                return lunchContainer;
            case "Dinner":
                return dinnerContainer;
            default:
                return null;
        }
    }

    private void removeMeal(String mealType) {
        FoodOption meal = selectedMeals.remove(mealType);
        if (meal != null) {
            Toast.makeText(this, meal.getFoodName() + " removed from your selection.", Toast.LENGTH_SHORT).show();
            displaySelectedMeals(mealType); // Update display
        }
    }

    private void removeMealDisplay(FoodOption meal, String mealType) {
        // Remove the existing meal display from the appropriate container
        LinearLayout selectedContainer = getMealContainer(mealType);
        if (selectedContainer != null) {
            selectedContainer.removeAllViews(); // Clear the previous selection
        }
    }

    private String generateRandomFoodName() {
        String[] foodNames = {"Pasta", "Salad", "Pizza", "Burger", "Sandwich", "Soup"};
        return foodNames[new Random().nextInt(foodNames.length)];
    }

    private int generateRandomCalories() {
        return 100 + new Random().nextInt(400); // Random calorie between 100 and 500
    }

    private int generateRandomAmount() {
        return 50 + new Random().nextInt(150); // Random amount between 50g and 200g
    }

    private static class FoodOption {
        private String foodName;
        private int calories;
        private int amount;

        public FoodOption(String foodName, int calories, int amount) {
            this.foodName = foodName;
            this.calories = calories;
            this.amount = amount;
        }

        public String getFoodName() {
            return foodName;
        }

        @Override
        public String toString() {
            return foodName + " - " + calories + " kcal (" + amount + "g)";
        }
    }
}
