package com.example.androidexample;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        textViewCalorieCounter = findViewById(R.id.textViewCalorieCounter);
        editTextExerciseName = findViewById(R.id.editTextExerciseName);
        editTextTimeSpent = findViewById(R.id.editTextTimeSpent);
        editTextCaloriesBurned = findViewById(R.id.editTextCaloriesBurned);
        buttonAddExercise = findViewById(R.id.buttonAddExercise);
        recyclerViewExercises = findViewById(R.id.recyclerViewExercises);

        // Set up RecyclerView
        exerciseList = new ArrayList<>();
        exerciseAdapter = new ExerciseAdapter(exerciseList);
        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExercises.setAdapter(exerciseAdapter);

        // Load existing exercises for today
        loadExercisesForToday();

        // Add Exercise Button Click Listener
        buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExercise();
            }
        });

        updateCalorieCounter();
    }

    private void loadExercisesForToday() {
        String url = BASE_URL + "/users/" + userId + "/fitness";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    exerciseList.clear();
                    int totalCaloriesBurned = 0;
                    String today = comparisonDateFormat.format(new Date());

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject exerciseJson = response.getJSONObject(i);
                            String name = exerciseJson.getString("name");
                            int timeSpent = exerciseJson.getInt("time");
                            int caloriesBurned = exerciseJson.getInt("calories");
                            String dateString = exerciseJson.getString("date");

                            // Parse the date from the server
                            Date exerciseDate = serverDateFormat.parse(dateString);
                            String exerciseDateOnly = comparisonDateFormat.format(exerciseDate);

                            // Check if the exercise date is today
                            if (exerciseDateOnly.equals(today)) {
                                Exercise exercise = new Exercise(name, timeSpent, caloriesBurned);
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
                },
                error -> Toast.makeText(ExerciseActivity.this, "Failed to load exercises", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
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
                            exerciseList.add(new Exercise(name, timeSpent, caloriesBurned));
                            exerciseAdapter.notifyItemInserted(exerciseList.size() - 1);
                            caloriesRemaining -= caloriesBurned;
                            updateCalorieCounter();
                            clearInputFields();
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
