package com.example.androidexample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SleepActivity extends AppCompatActivity {

    private EditText sleepInput;
    private EditText dayInput;
    private TextView feedbackMessage;
    private TextView statsTextView;
    private SleepGraphView sleepGraphView;
    private List<Float> sleepData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        // Initialize views
        sleepInput = findViewById(R.id.sleepInput);
        dayInput = findViewById(R.id.dayInput);
        feedbackMessage = findViewById(R.id.feedbackMessage);
        statsTextView = findViewById(R.id.statsTextView);
        Button submitButton = findViewById(R.id.submitButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        sleepGraphView = findViewById(R.id.sleepGraphView);

        // Initialize sleep data with fake data
        sleepData = new ArrayList<>();
        initializeFakeData();

        // Update graph and stats
        updateGraphAndStats();

        // Handle submit button (Add or Edit Sleep Hours)
        submitButton.setOnClickListener(v -> {
            String inputText = sleepInput.getText().toString();
            String dayText = dayInput.getText().toString();

            if (!inputText.isEmpty() && !dayText.isEmpty()) {
                try {
                    float sleepHours = Float.parseFloat(inputText);
                    int day = Integer.parseInt(dayText);

                    if (day < 1 || day > 7) {
                        feedbackMessage.setText("Invalid day. Enter a day between 1 and 7.");
                        return;
                    }

                    editSleepData(day - 1, sleepHours);
                    showFeedback(sleepHours);
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

                    deleteSleepData(day - 1);
                    updateGraphAndStats();
                } catch (NumberFormatException e) {
                    feedbackMessage.setText("Invalid input. Please enter a valid day.");
                }
            }
        });
    }

    private void initializeFakeData() {
        for (int i = 0; i < 7; i++) {
            sleepData.add((float) (5 + Math.random() * 3)); // Random hours between 5 and 8
        }
    }

    private void editSleepData(int dayIndex, float sleepHours) {
        sleepData.set(dayIndex, sleepHours);
    }

    private void deleteSleepData(int dayIndex) {
        sleepData.set(dayIndex, 0.0f); // Set sleep hours to 0 for the specified day
    }

    private void updateGraphAndStats() {
        sleepGraphView.setSleepData(sleepData);
        statsTextView.setText("Average Sleep: " + calculateAverageSleep() + " hours");
    }

    private float calculateAverageSleep() {
        float total = 0;
        int count = 0;

        for (float hours : sleepData) {
            if (hours > 0) {
                total += hours;
                count++;
            }
        }

        return count > 0 ? total / count : 0;
    }

    private void showFeedback(float sleepHours) {
        if (sleepHours >= 6 && sleepHours <= 8) {
            feedbackMessage.setText("Great job! You're within the healthy range.");
        } else if (sleepHours < 6) {
            feedbackMessage.setText("Aim for 6-8 hours of sleep for better health.");
        } else {
            feedbackMessage.setText("Good job, but try not to oversleep.");
        }
    }
}
