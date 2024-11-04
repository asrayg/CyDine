package com.example.androidexample;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exerciseList;
    private Context context;
    private final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080";

    public ExerciseAdapter(List<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.textViewExerciseName.setText(exercise.getName());
        holder.textViewTimeSpent.setText("Time: " + exercise.getTimeSpent() + " mins");
        holder.textViewCaloriesBurned.setText("Calories: " + exercise.getCaloriesBurned());

        // Edit button listener
        holder.buttonEdit.setOnClickListener(v -> showEditDialog(exercise, position));

        // Delete button listener
        holder.buttonDelete.setOnClickListener(v -> deleteExercise(exercise.getId(), position));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    private void showEditDialog(Exercise exercise, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Exercise");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_exercise, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        EditText editTextCalories = dialogView.findViewById(R.id.editTextCalories);

        editTextName.setText(exercise.getName());
        editTextTime.setText(String.valueOf(exercise.getTimeSpent()));
        editTextCalories.setText(String.valueOf(exercise.getCaloriesBurned()));

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = editTextName.getText().toString().trim();
            int newTime = Integer.parseInt(editTextTime.getText().toString().trim());
            int newCalories = Integer.parseInt(editTextCalories.getText().toString().trim());

            updateExercise(exercise.getId(), newName, newTime, newCalories, position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateExercise(int id, String name, int timeSpent, int caloriesBurned, int position) {
        String url = BASE_URL + "/fitness/" + id;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("time", timeSpent);
            requestBody.put("calories", caloriesBurned);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                response -> {
                    exerciseList.get(position).setName(name);
                    exerciseList.get(position).setTimeSpent(timeSpent);
                    exerciseList.get(position).setCaloriesBurned(caloriesBurned);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Exercise updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(context, "Failed to update exercise", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void deleteExercise(int id, int position) {
        String url = BASE_URL + "/fitness/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    exerciseList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Exercise deleted", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(context, "Failed to delete exercise", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExerciseName, textViewTimeSpent, textViewCaloriesBurned;
        Button buttonEdit, buttonDelete;

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
