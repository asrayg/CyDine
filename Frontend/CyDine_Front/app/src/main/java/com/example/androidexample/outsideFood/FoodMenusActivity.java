package com.example.androidexample.outsideFood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.StringRequest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.main.HomeScreenActivity;
import com.example.androidexample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * adding outside food item class
 */
public class FoodMenusActivity extends AppCompatActivity {

    private LinearLayout foodListLayout;
    private int currentCalories = 2000; // To track calories remaining
    private int totalProtein = 0, totalCarbs = 0, totalFat = 0; // Track macros
    private TextView caloriesTextView, proteinTextView, carbsTextView, fatTextView;

    private List<Integer> selectedFoodIds = new ArrayList<>();
    private int totalSelectedProtein = 0;
    private int totalSelectedCarbs = 0;
    private int totalSelectedFat = 0;
    private int totalSelectedCalories = 0;

    private int lastId = 0;
    private String userId; // Add this


    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menus);

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            userId = "1"; // Default value if "userId" is not found in the intent
        }


        foodListLayout = findViewById(R.id.food_list);
        caloriesTextView = findViewById(R.id.calories_text);
        proteinTextView = findViewById(R.id.protein_text);
        carbsTextView = findViewById(R.id.carbs_text);
        fatTextView = findViewById(R.id.fat_text);
        Button doneButton = findViewById(R.id.done_button);

        caloriesTextView.setText(String.valueOf(currentCalories));
        proteinTextView.setText(String.valueOf(totalProtein));
        carbsTextView.setText(String.valueOf(totalCarbs));
        fatTextView.setText(String.valueOf(totalFat));

        fetchFoodItemsFromAPI();

        setupAddFoodItem();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEmptyMealPlan();
                Intent intent = new Intent(FoodMenusActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchFoodItemsFromAPI() {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users/" + userId + "/FoodItems";
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

                        foodList.sort((f1, f2) -> {
                            try {
                                return f2.getInt("id") - f1.getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        });

                        if (!foodList.isEmpty()) {
                            try {
                                lastId = foodList.get(0).getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for (JSONObject food : foodList) {
                            try {
                                addFoodToUI(food.getString("name"),
                                        food.getInt("protein"),
                                        food.getInt("carbs"),
                                        food.getInt("fat"),
                                        food.getInt("calories"),
                                        food.getInt("id"));

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
                String name = nameInput.getText().toString().trim();
                String proteinStr = proteinInput.getText().toString().trim();
                String carbsStr = carbsInput.getText().toString().trim();
                String fatStr = fatInput.getText().toString().trim();
                String caloriesStr = caloriesInput.getText().toString().trim();

                if (name.isEmpty() || proteinStr.isEmpty() || carbsStr.isEmpty() || fatStr.isEmpty() || caloriesStr.isEmpty()) {
                    return;
                }

                int protein, carbs, fat, calories;
                try {
                    protein = Integer.parseInt(proteinStr);
                    carbs = Integer.parseInt(carbsStr);
                    fat = Integer.parseInt(fatStr);
                    calories = Integer.parseInt(caloriesStr);
                } catch (NumberFormatException e) {
                    return;
                }

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

                sendFoodItemToAPI(newFoodItem);

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

        try {
            newFoodItem.put("userId", userId); // Add userId to the JSON payload
            addFoodToUI(newFoodItem.getString("name"),
                    newFoodItem.getInt("protein"),
                    newFoodItem.getInt("carbs"),
                    newFoodItem.getInt("fat"),
                    newFoodItem.getInt("calories"),
                    -1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, newFoodItem,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(postRequest);
    }


    private void addFoodToUI(String name, int protein, int carbs, int fat, int calories, int foodId) {
        LinearLayout foodItemLayout = new LinearLayout(this);
        foodItemLayout.setOrientation(LinearLayout.VERTICAL);
        foodItemLayout.setPadding(16, 16, 16, 16);
        foodItemLayout.setBackgroundColor(getResources().getColor(R.color.red));

        TextView foodTextView = new TextView(this);
        foodTextView.setText("Name: " + name +
                "\nProtein: " + protein +
                "\nCarbs: " + carbs +
                "\nFat: " + fat +
                "\nCalories: " + calories);
        foodTextView.setTextSize(16);
        foodTextView.setPadding(0, 0, 0, 8);

        // Buttons layout
        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Button addMacroButton = new Button(this);
        addMacroButton.setText("+");
        addMacroButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        addMacroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodIds.add(foodId);
                updateMacros(protein, carbs, fat, calories);
            }
        });

        Button subtractMacroButton = new Button(this);
        subtractMacroButton.setText("-");
        subtractMacroButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        subtractMacroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodIds.remove(Integer.valueOf(foodId));
                reverseMacros(protein, carbs, fat, calories);
            }
        });

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

        buttonsLayout.addView(addMacroButton);
        buttonsLayout.addView(subtractMacroButton);
        buttonsLayout.addView(editButton);
        buttonsLayout.addView(deleteButton);

        foodItemLayout.addView(foodTextView);
        foodItemLayout.addView(buttonsLayout);

        foodListLayout.addView(foodItemLayout);
    }

    private void sendDeleteRequestToAPI(int foodId, LinearLayout foodItemLayout) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem/" + foodId; // Construct the DELETE URL with the foodId

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
        totalProtein += protein;
        totalCarbs += carbs;
        totalFat += fat;
        totalSelectedProtein = totalProtein;
        totalSelectedCarbs = totalCarbs;
        totalSelectedFat = totalFat;


        proteinTextView.setText(String.valueOf(totalProtein));
        carbsTextView.setText(String.valueOf(totalCarbs));
        fatTextView.setText(String.valueOf(totalFat));

        currentCalories -= calories;
        totalSelectedCalories += calories;

        caloriesTextView.setText(String.valueOf(currentCalories));
    }

    private void reverseMacros(int protein, int carbs, int fat, int calories) {
        totalProtein -= protein;
        totalCarbs -= carbs;
        totalFat -= fat;

        totalSelectedProtein = totalProtein;
        totalSelectedCarbs = totalCarbs;
        totalSelectedFat = totalFat;

        proteinTextView.setText(String.valueOf(totalProtein));
        carbsTextView.setText(String.valueOf(totalCarbs));
        fatTextView.setText(String.valueOf(totalFat));

        currentCalories += calories;
        totalSelectedCalories -= calories;

        caloriesTextView.setText(String.valueOf(currentCalories));
    }

    private void openEditDialog(LinearLayout foodItemLayout, TextView foodTextView, String currentName, int currentProtein, int currentCarbs, int currentFat, int oldCalories, int foodId) {
        foodItemLayout.removeAllViews();

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

        Button saveButton = new Button(this);
        saveButton.setText("Save");

        LinearLayout editLayout = new LinearLayout(this);
        editLayout.setOrientation(LinearLayout.VERTICAL);
        editLayout.setPadding(8, 8, 8, 8);

        editLayout.addView(nameInput);
        editLayout.addView(proteinInput);
        editLayout.addView(carbsInput);
        editLayout.addView(fatInput);
        editLayout.addView(caloriesInput);
        editLayout.addView(saveButton);

        foodItemLayout.addView(editLayout);

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
                    return;
                }

                foodTextView.setText("Name: " + newName +
                        "\nProtein: " + newProtein +
                        "\nCarbs: " + newCarbs +
                        "\nFat: " + newFat +
                        "\nCalories: " + newCalories);

                sendUpdatedFoodToAPI(newName, newProtein, newCarbs, newFat, newCalories, foodId);

                foodItemLayout.removeAllViews();
                addFoodToUI(newName, newProtein, newCarbs, newFat, newCalories, foodId);
            }
        });
    }

    private void sendUpdatedFoodToAPI(String name, int protein, int carbs, int fat, int calories, int foodId) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/FoodItem/" + foodId;

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


    private void postEmptyMealPlan() {
        JSONObject emptyMealPlan = new JSONObject();
        try {
            emptyMealPlan.put("protein", 0);
            emptyMealPlan.put("carbs", 0);
            emptyMealPlan.put("fat", 0);
            emptyMealPlan.put("finalCalories", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int mealPlanId = Integer.parseInt(response.trim());
                            Log.d("API_RESPONSE", "Empty meal plan created with ID: " + mealPlanId);
                            updateMealPlan(mealPlanId);
                        } catch (NumberFormatException e) {
                            Log.e("API_ERROR", "Failed to parse meal plan ID", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API_ERROR", "Failed to post empty meal plan", error);
                    }
                }) {
            @Override
            public byte[] getBody() {
                return emptyMealPlan.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        queue.add(postRequest);
    }


    private void updateMealPlan(int mealPlanId) {
        StringBuilder foodIdsBuilder = new StringBuilder();
        for (int foodId : selectedFoodIds) {
            if (foodIdsBuilder.length() > 0) {
                foodIdsBuilder.append(",");
            }
            foodIdsBuilder.append(foodId);
        }

        String url = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans/" + mealPlanId + "/fooditems/add/byId/" + userId;

        Log.d("MEAL_PLAN", "Selected food IDs: " + foodIdsBuilder.toString());
        Log.d("MEAL_PLAN", "Protein: " + totalSelectedProtein);
        Log.d("MEAL_PLAN", "Carbs: " + totalSelectedCarbs);
        Log.d("MEAL_PLAN", "Fat: " + totalSelectedFat);
        Log.d("MEAL_PLAN", "Final Calories: " + totalSelectedCalories);

        // Create a new JSONObject for this request
        JSONObject mealPlanUpdate = new JSONObject();
        try {
            mealPlanUpdate.put("foodItems", foodIdsBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        // Use StringRequest and assign it to putRequest
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API_RESPONSE", "Meal plan updated successfully: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API_ERROR", "Failed to update meal plan", error);
                    }
                }) {
            @Override
            public byte[] getBody() {
                return mealPlanUpdate.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        queue.add(putRequest); // Add the request to the queue
    }
}