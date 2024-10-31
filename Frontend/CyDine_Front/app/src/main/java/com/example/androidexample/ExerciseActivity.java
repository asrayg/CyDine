package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private TextView textViewCalorieCounter;
    private EditText editTextExerciseName, editTextTimeSpent, editTextCaloriesBurned;
    private Button buttonAddExercise;
    private RecyclerView recyclerViewExercises;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exerciseList;
    private int caloriesRemaining = 500;

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

        // Add Exercise Button Click Listener
        buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExercise();
            }
        });

        updateCalorieCounter();
    }

    private void addExercise() {
        String name = editTextExerciseName.getText().toString().trim();
        String timeSpentText = editTextTimeSpent.getText().toString().trim();
        String caloriesBurnedText = editTextCaloriesBurned.getText().toString().trim();

        if (!name.isEmpty() && !timeSpentText.isEmpty() && !caloriesBurnedText.isEmpty()) {
            int timeSpent = Integer.parseInt(timeSpentText);
            int caloriesBurned = Integer.parseInt(caloriesBurnedText);

            // Add exercise to list and update calories
            exerciseList.add(new Exercise(name, timeSpent, caloriesBurned));
            exerciseAdapter.notifyItemInserted(exerciseList.size() - 1);

            caloriesRemaining -= caloriesBurned;
            updateCalorieCounter();

            // Clear input fields
            editTextExerciseName.setText("");
            editTextTimeSpent.setText("");
            editTextCaloriesBurned.setText("");
        }
    }

    private void updateCalorieCounter() {
        textViewCalorieCounter.setText("Calories Remaining: " + caloriesRemaining);
    }
    }
