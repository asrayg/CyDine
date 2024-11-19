package com.example.androidexample.exercise;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter class for managing and displaying exercises in a RecyclerView.
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exerciseList;  // List of exercises to display
    private Context context;  // Context to access resources and perform actions like Toast
    private final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080";  // Base URL for API requests

    /**
     * Constructor to initialize the adapter with exercise list and context.
     * @param exerciseList List of exercises.
     * @param context The application context.
     */
    public ExerciseAdapter(List<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    /**
     * Creates a new ViewHolder instance when a new item is created.
     * @param parent The parent ViewGroup.
     * @param viewType The type of view.
     * @return A new ExerciseViewHolder instance.
     */
    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout for each exercise item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    /**
     * Binds data to each view item (exercise).
     * @param holder The ExerciseViewHolder.
     * @param position The position of the exercise in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.textViewExerciseName.setText(exercise.getName());  // Set exercise name
        holder.textViewTimeSpent.setText("Time: " + exercise.getTimeSpent() + " mins");  // Set time spent
        holder.textViewCaloriesBurned.setText("Calories: " + exercise.getCaloriesBurned());  // Set calories burned

        // Set listener for the Edit button
        holder.buttonEdit.setOnClickListener(v -> showEditDialog(exercise, position));

        // Set listener for the Delete button
        holder.buttonDelete.setOnClickListener(v -> deleteExercise(exercise.getId(), position));
    }

    /**
     * Returns the number of items in the exercise list.
     * @return The size of the exercise list.
     */
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    /**
     * Shows an AlertDialog for editing an exercise.
     * @param exercise The exercise to be edited.
     * @param position The position of the exercise in the list.
     */
    private void showEditDialog(Exercise exercise, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Exercise");

        // Inflate custom dialog view
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_exercise, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        EditText editTextCalories = dialogView.findViewById(R.id.editTextCalories);

        // Pre-fill the dialog with current exercise details
        editTextName.setText(exercise.getName());
        editTextTime.setText(String.valueOf(exercise.getTimeSpent()));
        editTextCalories.setText(String.valueOf(exercise.getCaloriesBurned()));

        // Set listener for Save button
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = editTextName.getText().toString().trim();
            int newTime = Integer.parseInt(editTextTime.getText().toString().trim());
            int newCalories = Integer.parseInt(editTextCalories.getText().toString().trim());

            // Update the exercise
            updateExercise(exercise.getId(), newName, newTime, newCalories, position);
        });

        // Set listener for Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Updates an existing exercise via an API PUT request.
     * @param id The ID of the exercise to update.
     * @param name The new name of the exercise.
     * @param timeSpent The new time spent on the exercise.
     * @param caloriesBurned The new calories burned during the exercise.
     * @param position The position of the exercise in the list to update the UI.
     */
    private void updateExercise(int id, String name, int timeSpent, int caloriesBurned, int position) {
        String url = BASE_URL + "/fitness/" + id;

        // Prepare the request body with the updated exercise data
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("time", timeSpent);
            requestBody.put("calories", caloriesBurned);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create PUT request to update exercise
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                response -> {
                    // Update exercise data in the list
                    exerciseList.get(position).setName(name);
                    exerciseList.get(position).setTimeSpent(timeSpent);
                    exerciseList.get(position).setCaloriesBurned(caloriesBurned);
                    notifyItemChanged(position);  // Notify RecyclerView to update the item
                    Toast.makeText(context, "Exercise updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(context, "Failed to update exercise", Toast.LENGTH_SHORT).show()
        );

        // Add request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Deletes an exercise via an API DELETE request.
     * @param id The ID of the exercise to delete.
     * @param position The position of the exercise in the list to remove from the UI.
     */
    private void deleteExercise(int id, int position) {
        String url = BASE_URL + "/fitness/" + id;

        // Create DELETE request to remove exercise
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    // Remove the exercise from the list and notify RecyclerView
                    exerciseList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Exercise deleted", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(context, "Failed to delete exercise", Toast.LENGTH_SHORT).show()
        );

        // Add request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * ViewHolder class to hold references to each exercise item view.
     */
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExerciseName, textViewTimeSpent, textViewCaloriesBurned;
        Button buttonEdit, buttonDelete;

        /**
         * Constructor to initialize the view elements for the exercise item.
         * @param itemView The root view for each exercise item.
         */
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExerciseName = itemView.findViewById(R.id.textViewExerciseName);
            textViewTimeSpent = itemView.findViewById(R.id.textViewTimeSpent);
            textViewCaloriesBurned = itemView.findViewById(R.id.textViewCaloriesBurned);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
