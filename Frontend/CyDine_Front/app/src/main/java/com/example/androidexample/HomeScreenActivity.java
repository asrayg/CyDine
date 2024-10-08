package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Find buttons
        Button foodMenusButton = findViewById(R.id.food_menus_button);
        Button profileButton = findViewById(R.id.profile_button);

        // Set listener for Food Menus button
        foodMenusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start FoodMenusActivity
                Intent intent = new Intent(HomeScreenActivity.this, FoodMenusActivity.class);
                startActivity(intent);
            }
        });

        // Set listener for Profile button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ProfileActivity
                Intent intent = new Intent(HomeScreenActivity.this, ProfileActivity.class);
                intent.putExtra("userId", getIntent().getStringExtra("userId"));
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                intent.putExtra("password", getIntent().getStringExtra("password"));
                startActivity(intent);
            }
        });
    }
}
