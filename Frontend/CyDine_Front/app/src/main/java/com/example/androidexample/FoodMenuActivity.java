package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Random;

public class FoodMenuActivity extends AppCompatActivity {

    private Spinner diningCenterSpinner;
    private Spinner mealTypeSpinner;
    private LinearLayout foodOptionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        diningCenterSpinner = findViewById(R.id.dining_center_spinner);
        mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        foodOptionsContainer = findViewById(R.id.food_options_container);
        Button generateButton = findViewById(R.id.generate_button);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateFoodOptions();
            }
        });
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

        cardView.addView(layout);
        return cardView;
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
}
