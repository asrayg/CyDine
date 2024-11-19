package com.example.androidexample;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;

import java.util.Map;

/**
 * This activity handles exercise tracking functionality.
 * Users can log exercises, view their daily calorie burn, and maintain a streak of activity.
 */
public class ExerciseActivity extends AppCompatActivity {

    // UI components for displaying and managing exercises
    private TextView textViewCalorieCounter; // Displays remaining calories
    private EditText editTextExerciseName, editTextTimeSpent, editTextCaloriesBurned; // Input fields for exercise details
    private Button buttonAddExercise; // Button to add a new exercise
    private RecyclerView recyclerViewExercises; // List of exercises
    private ExerciseAdapter exerciseAdapter; // Adapter for the RecyclerView
    private List<Exercise> exerciseList; // List to store exercise data

    // Variables for calorie tracking
    private int caloriesRemaining = 500; // Initial calories remaining
    private String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080"; // Base URL for backend API
    private String userId = "1"; // Placeholder for user ID, replace as necessary

    // Date formatting for server and local date parsing
    private SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
    private SimpleDateFormat comparisonDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private TextView textViewStreak; // Displays the user's exercise streak

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Retrieve userId passed via intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "User ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        textViewStreak = findViewById(R.id.textViewStreak);
        textViewCalorieCounter = findViewById(R.id.textViewCalorieCounter);
        editTextExerciseName = findViewById(R.id.editTextExerciseName);
        editTextTimeSpent = findViewById(R.id.editTextTimeSpent);
        editTextCaloriesBurned = findViewById(R.id.editTextCaloriesBurned);
        buttonAddExercise = findViewById(R.id.buttonAddExercise);
        recyclerViewExercises = findViewById(R.id.recyclerViewExercises);

        // Setup RecyclerView for displaying exercises
        exerciseList = new ArrayList<>();
        exerciseAdapter = new ExerciseAdapter(exerciseList, ExerciseActivity.this);
        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExercises.setAdapter(exerciseAdapter);

        // Load user streak and exercises for today
        loadUserStreak();
        loadExercisesForToday();

        // Set up button click listener for adding exercises
        buttonAddExercise.setOnClickListener(v -> addExercise());

        // Update calorie counter based on initial values
        updateCalorieCounter();
    }

    /**
     * Fetches the user's exercise streak from the backend and displays it.
     */
    private void loadUserStreak() {
        String url = BASE_URL + "/users/" + userId + "/streak";
        Log.d(TAG, url);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        int streak = response.getInt("streak");
                        textViewStreak.setText("Streak: " + streak + " days");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    textViewStreak.setText("Streak: 0 days");
                }
        );

        // Add request to the Volley request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * Loads exercises logged for the current day and updates the UI.
     */
    private void loadExercisesForToday() {
        String url = BASE_URL + "/users/" + userId + "/fitness";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    exerciseList.clear(); // Clear existing list
                    int totalCaloriesBurned = 0;
                    String today = comparisonDateFormat.format(new Date()); // Get today's date

                    // Map to store calories burned for each day
                    Map<String, Integer> dailyCalories = new HashMap<>();

                    // Parse response to extract exercise data
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject exerciseJson = response.getJSONObject(i);
                            int id = exerciseJson.getInt("id");
                            String name = exerciseJson.getString("name");
                            int timeSpent = exerciseJson.getInt("time");
                            int caloriesBurned = exerciseJson.getInt("calories");
                            String dateString = exerciseJson.getString("date");

                            // Parse server date format
                            Date exerciseDate = serverDateFormat.parse(dateString);
                            String exerciseDateOnly = comparisonDateFormat.format(exerciseDate);

                            // Update daily calories burned
                            dailyCalories.put(exerciseDateOnly,
                                    dailyCalories.getOrDefault(exerciseDateOnly, 0) + caloriesBurned);

                            // If exercise is from today, add it to the list
                            if (exerciseDateOnly.equals(today)) {
                                Exercise exercise = new Exercise(id, name, timeSpent, caloriesBurned);
                                exerciseList.add(exercise);
                                totalCaloriesBurned += caloriesBurned;
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    // Update the UI with the fetched data
                    exerciseAdapter.notifyDataSetChanged();
                    caloriesRemaining = 500 - totalCaloriesBurned; // Update remaining calories
                    updateCalorieCounter();

                    // Update daily calorie burns for the last 5 days
                    updateDailyCalories(dailyCalories);
                },
                error -> Toast.makeText(ExerciseActivity.this, "Failed to load exercises", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * Displays the daily calorie burns for the last 5 days.
     */
    private void updateDailyCalories(Map<String, Integer> dailyCalories) {
        // Generate a list of the last 5 days
        List<String> lastFiveDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            lastFiveDays.add(comparisonDateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Build a summary string for daily calorie burns
        StringBuilder dailyCalorieText = new StringBuilder("Daily Calories Burned:\n");
        for (String date : lastFiveDays) {
            int calories = dailyCalories.getOrDefault(date, 0);
            dailyCalorieText.append(date).append(": ").append(calories).append(" kcal\n");
        }

        // Display the daily calorie burns in the TextView
        TextView textViewDailyCalories = findViewById(R.id.textViewDailyCalories);
        textViewDailyCalories.setText(dailyCalorieText.toString());
    }

    /**
     * Handles adding a new exercise entry.
     */
    private void addExercise() {
        // Get input values
        String name = editTextExerciseName.getText().toString().trim();
        String timeSpentText = editTextTimeSpent.getText().toString().trim();
        String caloriesBurnedText = editTextCaloriesBurned.getText().toString().trim();

        // Validate input
        if (!name.isEmpty() && !timeSpentText.isEmpty() && !caloriesBurnedText.isEmpty()) {
            int timeSpent = Integer.parseInt(timeSpentText);
            int caloriesBurned = Integer.parseInt(caloriesBurnedText);

            // Create JSON object for the new exercise
            JSONObject exerciseJson = new JSONObject();
            try {
                exerciseJson.put("name", name);
                exerciseJson.put("time", timeSpent);
                exerciseJson.put("calories", caloriesBurned);
                exerciseJson.put("userId", userId);

                String url = BASE_URL + "/fitness";

                // Send POST request to add the exercise
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST, url, exerciseJson,
                        response -> {
                            try {
                                int id = response.getInt("id"); // Retrieve ID from the response
                                Exercise exercise = new Exercise(id, name, timeSpent, caloriesBurned);
                                exerciseList.add(exercise); // Add exercise to the list
                                exerciseAdapter.notifyItemInserted(exerciseList.size() - 1);
                                caloriesRemaining -= caloriesBurned; // Update remaining calories
                                updateCalorieCounter(); // Update the calorie counter UI
                                clearInputFields(); // Clear input fields
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Toast.makeText(ExerciseActivity.this, "Failed to add exercise", Toast.LENGTH_SHORT).show()
                );

                // Add request to the Volley request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clears the input fields for adding a new exercise.
     */
    private void clearInputFields() {
        editTextExerciseName.setText("");
        editTextTimeSpent.setText("");
        editTextCaloriesBurned.setText("");
    }

    /**
     * Updates the calorie counter TextView with the remaining calories.
     */
    private void updateCalorieCounter() {
        textViewCalorieCounter.setText("Calories Remaining: " + caloriesRemaining);
    }
}
