package com.example.androidexample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WeightActivity extends AppCompatActivity {

    private EditText weightInput;
    private EditText dayInput;
    private TextView feedbackMessage;
    private TextView statsTextView;
    private WeightGraphView weightGraphView;
    private List<Float> weightData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Initialize views
        weightInput = findViewById(R.id.weightInput);
        dayInput = findViewById(R.id.dayInput);
        feedbackMessage = findViewById(R.id.feedbackMessage);
        statsTextView = findViewById(R.id.statsTextView);
        Button submitButton = findViewById(R.id.submitButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        weightGraphView = findViewById(R.id.weightGraphView);

        // Initialize weight data with fake data
        weightData = new ArrayList<>();
        initializeFakeData();

        // Update graph and stats
        updateGraphAndStats();

        // Handle submit button (Add or Edit Weight Data)
        submitButton.setOnClickListener(v -> {
            String inputText = weightInput.getText().toString();
            String dayText = dayInput.getText().toString();

            if (!inputText.isEmpty() && !dayText.isEmpty()) {
                try {
                    float weight = Float.parseFloat(inputText);
                    int day = Integer.parseInt(dayText);

                    if (day < 1 || day > 7) {
                        feedbackMessage.setText("Invalid day. Enter a day between 1 and 7.");
                        return;
                    }

                    editWeightData(day - 1, weight);
                    showFeedback(weight, day - 1);
                    updateGraphAndStats();
                } catch (NumberFormatException e) {
                    feedbackMessage.setText("Invalid input. Please enter valid numbers.");
                }
            }
        });

        // Handle delete button
        deleteButton.setOnClickListener(v -> {
            String dayText = dayInput.getText().toString();

            if (!dayText.isEmpty()) {
                try {
                    int day = Integer.parseInt(dayText);

                    if (day < 1 || day > 7) {
                        feedbackMessage.setText("Invalid day. Enter a day between 1 and 7.");
                        return;
                    }

                    deleteWeightData(day - 1);
                    updateGraphAndStats();
                } catch (NumberFormatException e) {
                    feedbackMessage.setText("Invalid input. Please enter a valid day.");
                }
            }
        });
    }

    private void initializeFakeData() {
        for (int i = 0; i < 7; i++) {
            weightData.add(70f + (float) (Math.random() * 5)); // Random weights around 70 kg
        }
    }

    private void editWeightData(int dayIndex, float weight) {
        weightData.set(dayIndex, weight);
    }

    private void deleteWeightData(int dayIndex) {
        weightData.set(dayIndex, 0.0f); // Set weight to 0 for the specified day
    }

    private void updateGraphAndStats() {
        weightGraphView.setWeightData(weightData);
        statsTextView.setText("Average Weight: " + calculateAverageWeight() + " kg");
    }

    private float calculateAverageWeight() {
        float total = 0;
        int count = 0;

        for (float weight : weightData) {
            if (weight > 0) {
                total += weight;
                count++;
            }
        }

        return count > 0 ? total / count : 0;
    }

    private void showFeedback(float weight, int dayIndex) {
        float previousWeight = (dayIndex > 0) ? weightData.get(dayIndex - 1) : 0;
        float change = weight - previousWeight;

        if (dayIndex == 0 || previousWeight == 0) {
            feedbackMessage.setText("Weight recorded: " + weight + " kg.");
        } else {
            feedbackMessage.setText("Weight change since last entry: " + change + " kg.");
        }
    }
}
