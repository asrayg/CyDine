package com.example.androidexample;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ExerciseActivity extends AppCompatActivity {

    private TextView textViewCalorieCounter;
    private EditText editTextExerciseName, editTextTimeSpent, editTextCaloriesBurned;
    private Button buttonAddExercise;
    private RecyclerView recyclerViewExercises;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exerciseList;
    private int caloriesRemaining = 500;
    private String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080";
    private String userId = "1";  // Replace this with the actual user ID
    private SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
    private SimpleDateFormat comparisonDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private TextView textViewStreak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Retrieve the userId from the intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "User ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textViewStreak = findViewById(R.id.textViewStreak);

        textViewCalorieCounter = findViewById(R.id.textViewCalorieCounter);
        editTextExerciseName = findViewById(R.id.editTextExerciseName);
        editTextTimeSpent = findViewById(R.id.editTextTimeSpent);
        editTextCaloriesBurned = findViewById(R.id.editTextCaloriesBurned);
        buttonAddExercise = findViewById(R.id.buttonAddExercise);
        recyclerViewExercises = findViewById(R.id.recyclerViewExercises);

        // Set up RecyclerView
        exerciseList = new ArrayList<>();
        exerciseAdapter = new ExerciseAdapter(exerciseList, ExerciseActivity.this);
        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExercises.setAdapter(exerciseAdapter);

        // Load existing exercises for today
        loadUserStreak();

        loadExercisesForToday();

        // Add Exercise Button Click Listener
        buttonAddExercise.setOnClickListener(v -> addExercise());

        updateCalorieCounter();
    }

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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }



    private void loadExercisesForToday() {
        String url = BASE_URL + "/users/" + userId + "/fitness";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    exerciseList.clear();
                    int totalCaloriesBurned = 0;
                    String today = comparisonDateFormat.format(new Date());

                    // Map to hold calories burned for each day
                    Map<String, Integer> dailyCalories = new HashMap<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject exerciseJson = response.getJSONObject(i);
                            int id = exerciseJson.getInt("id");
                            String name = exerciseJson.getString("name");
                            int timeSpent = exerciseJson.getInt("time");
                            int caloriesBurned = exerciseJson.getInt("calories");
                            String dateString = exerciseJson.getString("date");

                            // Parse the date from the server
                            Date exerciseDate = serverDateFormat.parse(dateString);
                            String exerciseDateOnly = comparisonDateFormat.format(exerciseDate);

                            // Track calories burned for each day
                            dailyCalories.put(exerciseDateOnly,
                                    dailyCalories.getOrDefault(exerciseDateOnly, 0) + caloriesBurned);

                            // Check if the exercise date is today
                            if (exerciseDateOnly.equals(today)) {
                                Exercise exercise = new Exercise(id, name, timeSpent, caloriesBurned);
                                exerciseList.add(exercise);
                                totalCaloriesBurned += caloriesBurned;
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    exerciseAdapter.notifyDataSetChanged();
                    caloriesRemaining = 500 - totalCaloriesBurned;
                    updateCalorieCounter();

                    // Calculate the calories burned for the last 5 days and display them
                    updateDailyCalories(dailyCalories);
                },
                error -> Toast.makeText(ExerciseActivity.this, "Failed to load exercises", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    private void updateDailyCalories(Map<String, Integer> dailyCalories) {
        // Get the last 5 days as a list of formatted date strings
        List<String> lastFiveDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            lastFiveDays.add(comparisonDateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Build a string to display the last 5 days' calorie burns
        StringBuilder dailyCalorieText = new StringBuilder("Daily Calories Burned:\n");
        for (String date : lastFiveDays) {
            int calories = dailyCalories.getOrDefault(date, 0);
            dailyCalorieText.append(date).append(": ").append(calories).append(" kcal\n");
        }

        // Update the TextView with daily calorie burns
        TextView textViewDailyCalories = findViewById(R.id.textViewDailyCalories);
        textViewDailyCalories.setText(dailyCalorieText.toString());
    }

    private void addExercise() {
        String name = editTextExerciseName.getText().toString().trim();
        String timeSpentText = editTextTimeSpent.getText().toString().trim();
        String caloriesBurnedText = editTextCaloriesBurned.getText().toString().trim();

        if (!name.isEmpty() && !timeSpentText.isEmpty() && !caloriesBurnedText.isEmpty()) {
            int timeSpent = Integer.parseInt(timeSpentText);
            int caloriesBurned = Integer.parseInt(caloriesBurnedText);

            JSONObject exerciseJson = new JSONObject();
            try {
                exerciseJson.put("name", name);
                exerciseJson.put("time", timeSpent);
                exerciseJson.put("calories", caloriesBurned);
                exerciseJson.put("userId", userId);

                String url = BASE_URL + "/fitness";

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST, url, exerciseJson,
                        response -> {
                            try {
                                int id = response.getInt("id"); // Retrieve the id from the response
                                Exercise exercise = new Exercise(id, name, timeSpent, caloriesBurned);
                                exerciseList.add(exercise);
                                exerciseAdapter.notifyItemInserted(exerciseList.size() - 1);
                                caloriesRemaining -= caloriesBurned;
                                updateCalorieCounter();
                                clearInputFields();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Toast.makeText(ExerciseActivity.this, "Failed to add exercise", Toast.LENGTH_SHORT).show()
                );

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCalorieCounter() {
        textViewCalorieCounter.setText("Calories Remaining: " + caloriesRemaining);
    }

    private void clearInputFields() {
        editTextExerciseName.setText("");
        editTextTimeSpent.setText("");
        editTextCaloriesBurned.setText("");
    }
}

