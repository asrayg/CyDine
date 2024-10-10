package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FoodMenusActivity extends AppCompatActivity {

    private LinearLayout foodListLayout;
    private int currentCalories = 2000; // To track calories remaining
    private int totalProtein = 0, totalCarbs = 0, totalFat = 0; // Track macros
    private TextView caloriesTextView, proteinTextView, carbsTextView, fatTextView;

    // Track the highest ID locally
    private int lastId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menus);

        // Initialize UI components
        foodListLayout = findViewById(R.id.food_list);
        caloriesTextView = findViewById(R.id.calories_text);
        proteinTextView = findViewById(R.id.protein_text);
        carbsTextView = findViewById(R.id.carbs_text);
        fatTextView = findViewById(R.id.fat_text);
        Button doneButton = findViewById(R.id.done_button);

        // Initialize calories and macros display
        caloriesTextView.setText(String.valueOf(currentCalories));
        proteinTextView.setText(String.valueOf(totalProtein));
        carbsTextView.setText(String.valueOf(totalCarbs));
        fatTextView.setText(String.valueOf(totalFat));

        // Fetch food items from the API and display them
        fetchFoodItemsFromAPI();

        // Setup add food functionality
        setupAddFoodItem();

        // Set up Done button click listener
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Done button (hook up to backend later)
                handleDoneButtonClick();
            }
        });
    }

    private void fetchFoodItemsFromAPI() {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<JSONObject> foodList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                foodList.add(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Sort the food list by ID in descending order
                        foodList.sort((f1, f2) -> {
                            try {
                                return f2.getInt("id") - f1.getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        });

                        // Get the last ID from the list
                        if (!foodList.isEmpty()) {
                            try {
                                lastId = foodList.get(0).getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Display the sorted food items
                        for (JSONObject food : foodList) {
                            try {
                                addFoodToUI(food.getString("name"),
                                        food.getInt("protein"),
                                        food.getInt("carbs"),
                                        food.getInt("fat"),
                                        food.getInt("calories"),
                                        food.getInt("id")); // Pass the 'id' field as the last parameter

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void setupAddFoodItem() {
        EditText nameInput = findViewById(R.id.food_name_input);
        EditText proteinInput = findViewById(R.id.food_protein_input);
        EditText carbsInput = findViewById(R.id.food_carbs_input);
        EditText fatInput = findViewById(R.id.food_fat_input);
        EditText caloriesInput = findViewById(R.id.food_calories_input);
        Button addButton = findViewById(R.id.add_food_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve and validate input data
                String name = nameInput.getText().toString().trim();
                String proteinStr = proteinInput.getText().toString().trim();
                String carbsStr = carbsInput.getText().toString().trim();
                String fatStr = fatInput.getText().toString().trim();
                String caloriesStr = caloriesInput.getText().toString().trim();

                if (name.isEmpty() || proteinStr.isEmpty() || carbsStr.isEmpty() || fatStr.isEmpty() || caloriesStr.isEmpty()) {
                    return; // Optionally show an error message
                }

                int protein, carbs, fat, calories;
                try {
                    protein = Integer.parseInt(proteinStr);
                    carbs = Integer.parseInt(carbsStr);
                    fat = Integer.parseInt(fatStr);
                    calories = Integer.parseInt(caloriesStr);
                } catch (NumberFormatException e) {
                    return; // Optionally show an error message
                }

                // Create JSON object for the new food item
                JSONObject newFoodItem = new JSONObject();
                try {
                    newFoodItem.put("name", name);
                    newFoodItem.put("protein", protein);
                    newFoodItem.put("carbs", carbs);
                    newFoodItem.put("fat", fat);
                    newFoodItem.put("calories", calories);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send the new food item to the backend
                sendFoodItemToAPI(newFoodItem);

                // Clear the input fields after adding
                nameInput.setText("");
                proteinInput.setText("");
                carbsInput.setText("");
                fatInput.setText("");
                caloriesInput.setText("");
            }
        });
    }
    private void sendFoodItemToAPI(JSONObject newFoodItem) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Add the item to the UI immediately
        try {
            // Manually add to the UI (optimistic UI update)
            addFoodToUI(newFoodItem.getString("name"),
                    newFoodItem.getInt("protein"),
                    newFoodItem.getInt("carbs"),
                    newFoodItem.getInt("fat"),
                    newFoodItem.getInt("calories"),
                    -1); // Pass the 'id' field as the last parameter


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, newFoodItem,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the server if needed
                        try {
                            // Add the new food item to the UI
                            addFoodToUI(response.getString("name"),
                                    response.getInt("protein"),
                                    response.getInt("carbs"),
                                    response.getInt("fat"),
                                    response.getInt("calories"),
                                    response.getInt("id")); // Pass the 'id' field as the last parameter


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error if the request fails
                        error.printStackTrace();
                    }
                });

        queue.add(postRequest);
    }


    private void addFoodToUI(String name, int protein, int carbs, int fat, int calories, int foodId) {
        // Create a vertical layout for each food item
        LinearLayout foodItemLayout = new LinearLayout(this);
        foodItemLayout.setOrientation(LinearLayout.VERTICAL);
        foodItemLayout.setPadding(16, 16, 16, 16);
        foodItemLayout.setBackgroundColor(getResources().getColor(R.color.red)); // Ensure R.color.red is defined

        // TextView to display food item details with line breaks
        TextView foodTextView = new TextView(this);
        foodTextView.setText("Name: " + name +
                "\nProtein: " + protein +
                "\nCarbs: " + carbs +
                "\nFat: " + fat +
                "\nCalories: " + calories);
        foodTextView.setTextSize(16);
        foodTextView.setPadding(0, 0, 0, 8); // Add spacing below the text

        // Buttons layout
        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // "+" Button to update macros and calories
        Button addMacroButton = new Button(this);
        addMacroButton.setText("+");
        addMacroButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        addMacroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMacros(protein, carbs, fat, calories);
            }
        });

        // "-" Button to reverse macros and calories
        Button subtractMacroButton = new Button(this);
        subtractMacroButton.setText("-");
        subtractMacroButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        subtractMacroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseMacros(protein, carbs, fat, calories);
            }
        });

        // "Edit" Button to edit the food item
        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call openEditDialog with the foodId
                openEditDialog(foodItemLayout, foodTextView, name, protein, carbs, fat, calories, foodId); // Pass foodId as the last parameter
            }
        });

        // "Delete" Button to remove the food item
        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDeleteRequestToAPI(foodId, foodItemLayout);
                foodListLayout.removeView(foodItemLayout);
            }
        });

        // Add buttons to the buttons layout
        buttonsLayout.addView(addMacroButton);
        buttonsLayout.addView(subtractMacroButton);
        buttonsLayout.addView(editButton);
        buttonsLayout.addView(deleteButton);

        // Add the TextView and buttons layout to the food item layout
        foodItemLayout.addView(foodTextView);
        foodItemLayout.addView(buttonsLayout);

        // Add the food item layout to the parent layout
        foodListLayout.addView(foodItemLayout);
    }

    private void sendDeleteRequestToAPI(int foodId, LinearLayout foodItemLayout) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem/" + foodId; // Construct the DELETE URL with the foodId

        // Send DELETE request to the backend to remove the food item
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On success, remove the item from the UI
                        foodListLayout.removeView(foodItemLayout);
                        Log.d("API_RESPONSE", "Food item deleted successfully: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error if the request fails
                        Log.e("API_ERROR", "Failed to delete food item", error);
                    }
                });

        queue.add(deleteRequest);
    }


    private void updateMacros(int protein, int carbs, int fat, int calories) {
        // Update the total protein, carbs, and fat counters
        totalProtein += protein;
        totalCarbs += carbs;
        totalFat += fat;

        // Update macros TextViews
        proteinTextView.setText(String.valueOf(totalProtein));
        carbsTextView.setText(String.valueOf(totalCarbs));
        fatTextView.setText(String.valueOf(totalFat));

        // Update the calorie count
        currentCalories -= calories;
        caloriesTextView.setText(String.valueOf(currentCalories));
    }

    private void reverseMacros(int protein, int carbs, int fat, int calories) {
        // Reverse the total protein, carbs, and fat counters
        totalProtein -= protein;
        totalCarbs -= carbs;
        totalFat -= fat;

        // Update macros TextViews
        proteinTextView.setText(String.valueOf(totalProtein));
        carbsTextView.setText(String.valueOf(totalCarbs));
        fatTextView.setText(String.valueOf(totalFat));

        // Restore the calorie count
        currentCalories += calories;
        caloriesTextView.setText(String.valueOf(currentCalories));
    }

    private void openEditDialog(LinearLayout foodItemLayout, TextView foodTextView, String currentName, int currentProtein, int currentCarbs, int currentFat, int oldCalories, int foodId) {
        // Remove current views
        foodItemLayout.removeAllViews();

        // Create EditTexts for editing
        EditText nameInput = new EditText(this);
        nameInput.setHint("Name");
        nameInput.setText(currentName);

        EditText proteinInput = new EditText(this);
        proteinInput.setHint("Protein");
        proteinInput.setText(String.valueOf(currentProtein));

        EditText carbsInput = new EditText(this);
        carbsInput.setHint("Carbs");
        carbsInput.setText(String.valueOf(currentCarbs));

        EditText fatInput = new EditText(this);
        fatInput.setHint("Fat");
        fatInput.setText(String.valueOf(currentFat));

        EditText caloriesInput = new EditText(this);
        caloriesInput.setHint("Calories");
        caloriesInput.setText(String.valueOf(oldCalories));

        // Button to save changes
        Button saveButton = new Button(this);
        saveButton.setText("Save");

        // Set up the layout for editing
        LinearLayout editLayout = new LinearLayout(this);
        editLayout.setOrientation(LinearLayout.VERTICAL);
        editLayout.setPadding(8, 8, 8, 8);

        editLayout.addView(nameInput);
        editLayout.addView(proteinInput);
        editLayout.addView(carbsInput);
        editLayout.addView(fatInput);
        editLayout.addView(caloriesInput);
        editLayout.addView(saveButton);

        // Add the edit layout to the food item layout
        foodItemLayout.addView(editLayout);

        // Handle save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameInput.getText().toString().trim();
                String proteinStr = proteinInput.getText().toString().trim();
                String carbsStr = carbsInput.getText().toString().trim();
                String fatStr = fatInput.getText().toString().trim();
                String caloriesStr = caloriesInput.getText().toString().trim();

                if (newName.isEmpty() || proteinStr.isEmpty() || carbsStr.isEmpty() || fatStr.isEmpty() || caloriesStr.isEmpty()) {
                    return; // Optionally show an error message
                }

                int newProtein, newCarbs, newFat, newCalories;
                try {
                    newProtein = Integer.parseInt(proteinStr);
                    newCarbs = Integer.parseInt(carbsStr);
                    newFat = Integer.parseInt(fatStr);
                    newCalories = Integer.parseInt(caloriesStr);
                } catch (NumberFormatException e) {
                    return; // Optionally show an error message
                }

                // Update the food item text in the UI
                foodTextView.setText("Name: " + newName +
                        "\nProtein: " + newProtein +
                        "\nCarbs: " + newCarbs +
                        "\nFat: " + newFat +
                        "\nCalories: " + newCalories);

                // Send updated food item to the backend, pass the foodId along with other parameters
                sendUpdatedFoodToAPI(newName, newProtein, newCarbs, newFat, newCalories, foodId);

                // Restore buttons after saving
                foodItemLayout.removeAllViews();
                addFoodToUI(newName, newProtein, newCarbs, newFat, newCalories, foodId);
            }
        });
    }

    private void sendUpdatedFoodToAPI(String name, int protein, int carbs, int fat, int calories, int foodId) {
        // Construct the URL to update the specific item by ID
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem/" + foodId;

        // Create JSON object for the updated food item
        JSONObject updatedFoodItem = new JSONObject();
        try {
            updatedFoodItem.put("name", name);
            updatedFoodItem.put("protein", protein);
            updatedFoodItem.put("carbs", carbs);
            updatedFoodItem.put("fat", fat);
            updatedFoodItem.put("calories", calories);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send PUT request to update the food item on the backend
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, updatedFoodItem,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("API_RESPONSE", "Food item updated successfully: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API_ERROR", "Failed to update food item", error);
                    }
                });

        queue.add(putRequest);
    }


    private void handleDoneButtonClick() {
        // This function will handle the Done button click.
        // In the future, this can submit the selected food items to the backend.
        // For now, you can optionally show a message or perform another action.
    }
}

