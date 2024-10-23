package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WaterActivity extends AppCompatActivity {
    private int waterGoal = 2000;  // Default goal
    private int currentWaterIntake = 0;
    private ProgressBar waterProgress;
    private TextView remainingWaterText, waterGoalText;
    private EditText waterInput, goalInput;
    private Button setGoalButton, confirmGoalButton, addButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        // Initialize views
        waterProgress = findViewById(R.id.water_goal_progress);
        remainingWaterText = findViewById(R.id.remaining_water_text);
        waterGoalText = findViewById(R.id.water_goal_text);
        waterInput = findViewById(R.id.water_input);
        goalInput = findViewById(R.id.goal_input);
        setGoalButton = findViewById(R.id.set_goal_button);
        confirmGoalButton = findViewById(R.id.confirm_goal_button);
        addButton = findViewById(R.id.add_button);
        resetButton = findViewById(R.id.reset_button);

        // Initial setup for water goal and progress
        updateProgress();

        // Add Water Button Click
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waterAmount = waterInput.getText().toString();
                if (!waterAmount.isEmpty()) {
                    int amount = Integer.parseInt(waterAmount);
                    adjustWaterIntake(amount);
                } else {
                    Toast.makeText(WaterActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Reset Button Click
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetWaterIntake();
            }
        });

        // Set Goal Button Click
        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalInput.setVisibility(View.VISIBLE);
                confirmGoalButton.setVisibility(View.VISIBLE);
            }
        });

        // Confirm Goal Button Click
        confirmGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalAmount = goalInput.getText().toString();
                if (!goalAmount.isEmpty()) {
                    int newGoal = Integer.parseInt(goalAmount);
                    setWaterGoal(newGoal);
                    goalInput.setVisibility(View.GONE);
                    confirmGoalButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(WaterActivity.this, "Please enter a valid goal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Adjust water intake, allowing both adding and removing water
    private void adjustWaterIntake(int amount) {
        currentWaterIntake += amount;

        // Ensure the intake does not exceed the goal or go below zero
        if (currentWaterIntake > waterGoal) {
            currentWaterIntake = waterGoal;
        } else if (currentWaterIntake < 0) {
            currentWaterIntake = 0;
        }

        updateProgress();
        waterInput.setText("");
    }

    // Reset water intake to 0
    private void resetWaterIntake() {
        currentWaterIntake = 0;
        updateProgress();
    }

    // Set a new water goal
    private void setWaterGoal(int newGoal) {
        waterGoal = newGoal;
        currentWaterIntake = 0;  // Reset the intake when setting a new goal
        waterProgress.setMax(waterGoal);  // Update the ProgressBar max value
        updateProgress();
    }

    // Update the progress bar and remaining text
    private void updateProgress() {
        waterProgress.setProgress(currentWaterIntake);
        int remaining = waterGoal - currentWaterIntake;
        remainingWaterText.setText("Remaining: " + remaining + " ml");
        waterGoalText.setText("Goal: " + currentWaterIntake + "/" + waterGoal + " ml");

        if (currentWaterIntake >= waterGoal) {
            Toast.makeText(this, "Congratulations! You've reached your water goal!", Toast.LENGTH_LONG).show();
        }
    }
}
