package com.example.androidexample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WaterActivity extends AppCompatActivity {
    private static final String TAG = "WaterActivity";
    private int waterGoal = 2000;
    private int currentWaterIntake = 0;
    private ProgressBar waterProgress;
    private TextView remainingWaterText, waterGoalText;
    private EditText waterInput, goalInput;
    private Button setGoalButton, confirmGoalButton, addButton, resetButton;
    private RequestQueue requestQueue;
    private String userId;  // Removed hardcoded userId

    private final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080";  // Replace with your backend URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        Log.d(TAG, "onCreate: Initializing views and request queue");

        // Retrieve the userId from the Intent
        userId = getIntent().getStringExtra("userId");  // Retrieve as String
        if (userId == null) {
            Log.e(TAG, "User ID is missing. Ensure HomeScreenActivity passes userId.");
            Toast.makeText(this, "User ID missing", Toast.LENGTH_SHORT).show();
        }

        requestQueue = Volley.newRequestQueue(this);

        // Post new entry on open
        postNewWaterEntry();
        fetchLast5DaysWaterIntake();




        // Initialize views and Volley request queue
        waterProgress = findViewById(R.id.water_goal_progress);
        remainingWaterText = findViewById(R.id.remaining_water_text);
        waterGoalText = findViewById(R.id.water_goal_text);
        waterInput = findViewById(R.id.water_input);
        goalInput = findViewById(R.id.goal_input);
        setGoalButton = findViewById(R.id.set_goal_button);
        confirmGoalButton = findViewById(R.id.confirm_goal_button);
        addButton = findViewById(R.id.add_button);
        requestQueue = Volley.newRequestQueue(this);

        Log.d(TAG, "onCreate: Fetching today's water intake");
        fetchTodayWaterIntake();  // Fetch water intake for today

        // Add Water Button Click
        addButton.setOnClickListener(v -> {
            String waterAmount = waterInput.getText().toString();
            Log.d(TAG, "Add Button Clicked: waterAmount = " + waterAmount);
            if (!waterAmount.isEmpty()) {
                int amount = Integer.parseInt(waterAmount);
                Log.d(TAG, "Parsed water amount: " + amount);
                addWaterIntake(amount);
            } else {
                Log.w(TAG, "Add Button Clicked: Invalid amount entered");
                Toast.makeText(WaterActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            }
        });




        // Set Goal Button Click
        setGoalButton.setOnClickListener(v -> {
            Log.d(TAG, "Set Goal Button Clicked");
            goalInput.setVisibility(View.VISIBLE);
            confirmGoalButton.setVisibility(View.VISIBLE);
        });

        // Confirm Goal Button Click
        confirmGoalButton.setOnClickListener(v -> {
            String goalAmount = goalInput.getText().toString();
            Log.d(TAG, "Confirm Goal Button Clicked: goalAmount = " + goalAmount);
            if (!goalAmount.isEmpty()) {
                int newGoal = Integer.parseInt(goalAmount);
                Log.d(TAG, "Parsed new goal: " + newGoal);
                setWaterGoal(newGoal);
                goalInput.setVisibility(View.GONE);
                confirmGoalButton.setVisibility(View.GONE);
            } else {
                Log.w(TAG, "Confirm Goal Button Clicked: Invalid goal entered");
                Toast.makeText(WaterActivity.this, "Please enter a valid goal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchLast5DaysWaterIntake() {
        String url = BASE_URL + "/users/" + userId + "/water/last5days";
        Log.d(TAG, "Fetching last 5 days water intake from URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d(TAG, "Received response for last 5 days water intake: " + response.toString());
                    LinearLayout last5DaysContainer = findViewById(R.id.last_5_days_container);
                    last5DaysContainer.removeAllViews(); // Clear previous data

                    // Loop through the last 5 records and create a view for each
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject entry = response.getJSONObject(i);
                            String date = entry.getString("date").split("T")[0];  // Extract just the date
                            int totalIntake = entry.getInt("total");

                            // Create a TextView for each day's intake
                            TextView intakeText = new TextView(this);
                            intakeText.setText("Date: " + date + " - Water Intake: " + totalIntake + " ml");
                            intakeText.setTextSize(16);
                            intakeText.setPadding(8, 8, 8, 8);
                            last5DaysContainer.addView(intakeText);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response for last 5 days", e);
                        }
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching last 5 days water intake", error);
                    Toast.makeText(WaterActivity.this, "Error fetching last 5 days data", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }



    private void postNewWaterEntry() {
        String url = BASE_URL + "/water";  // Use /water as per the backend endpoint
        Log.d(TAG, "Posting a new water entry to URL: " + url);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("goal", waterGoal);
            jsonBody.put("total", 0); // New entry with 0 intake
            jsonBody.put("userId", userId); // Add userId in the body as the backend expects it
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON body for new entry", e);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Log.d(TAG, "New water entry created. Server response: " + response),
                error -> Log.e(TAG, "Error posting new water entry", error)
        ) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void fetchTodayWaterIntake() {
        String url = BASE_URL + "/users/" + userId + "/water/today";
        Log.d(TAG, "Fetching today's water intake from URL: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d(TAG, "Received response for today's water intake: " + response.toString());
                    try {
                        waterGoal = response.getInt("goal");
                        currentWaterIntake = response.getInt("total");
                        Log.d(TAG, "Parsed waterGoal: " + waterGoal + ", currentWaterIntake: " + currentWaterIntake);
                        waterProgress.setMax(waterGoal);  // Set ProgressBar max to the goal
                        updateProgress();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(WaterActivity.this, "Error parsing data. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching today's water intake", error);
                    if (error instanceof ServerError) {
                        Toast.makeText(WaterActivity.this, "Server error. Please try again later.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(WaterActivity.this, "Network error. Please check your connection.", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }



    private void addWaterIntake(int amount) {
        String url = BASE_URL + "/users/" + userId + "/water/today/drank/" + amount;
        Log.d(TAG, "Adding water intake. URL: " + url + ", Amount: " + amount);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                response -> {
                    Log.d(TAG, "Received response after adding water intake: " + response.toString());
                    try {
                        currentWaterIntake = response.getInt("total");
                        Log.d(TAG, "Updated currentWaterIntake: " + currentWaterIntake);
                        updateProgress();
                        waterInput.setText("");
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(WaterActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error updating water intake", error);
                    Toast.makeText(WaterActivity.this, "Error updating intake: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void setWaterGoal(int newGoal) {
        String url = BASE_URL + "/users/" + userId + "/water/today/goal/" + newGoal;
        Log.d(TAG, "Setting new water goal. URL: " + url + ", New Goal: " + newGoal);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                response -> {
                    Log.d(TAG, "Received response after setting new goal: " + response.toString());
                    try {
                        waterGoal = response.getInt("goal");
                        Log.d(TAG, "Updated waterGoal: " + waterGoal);
                        waterProgress.setMax(waterGoal);
                        updateProgress();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(WaterActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error setting new water goal", error);
                    Toast.makeText(WaterActivity.this, "Error setting goal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            Log.d(TAG, "Hiding keyboard");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.d(TAG, "No view in focus, keyboard not hidden");
        }
    }



    private void updateProgress() {
        Log.d(TAG, "Updating progress bar and texts");
        waterProgress.setProgress(currentWaterIntake);
        int remaining = waterGoal - currentWaterIntake;
        remainingWaterText.setText("Remaining: " + remaining + " ml");
        waterGoalText.setText("Goal: " + currentWaterIntake + "/" + waterGoal + " ml");

        Log.d(TAG, "CurrentWaterIntake: " + currentWaterIntake + ", WaterGoal: " + waterGoal);

        if (currentWaterIntake >= waterGoal) {
            Log.d(TAG, "Water goal reached");
            Toast.makeText(this, "Congratulations! You've reached your water goal!", Toast.LENGTH_LONG).show();
        }
    }
}
