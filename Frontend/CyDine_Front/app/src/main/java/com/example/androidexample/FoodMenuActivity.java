package com.example.androidexample;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Outside food tracker class
 */
public class FoodMenuActivity extends AppCompatActivity {

    private Spinner diningCenterSpinner;
    private Spinner mealTypeSpinner;
    private LinearLayout foodOptionsContainer;
    private LinearLayout breakfastContainer;
    private LinearLayout lunchContainer;
    private LinearLayout dinnerContainer;
    private LinearLayout lateNightContainer;
    private String userId;
    private Button save;
    private int mealPlanID;
    private int mealID;

    private Map<String, List<FoodOption>> selectedMeals; // Store selected meals by meal type
    private List<JSONObject> selectedMealsJson; // List to store JSON objects of selected meals
    private static final String ADD_MEAL_URL = "http://coms-3090-020.class.las.iastate.edu:8080/DHmealplans"; // Replace wit


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        userId = getIntent().getStringExtra("userId");
        selectedMealsJson = new ArrayList<>();

        diningCenterSpinner = findViewById(R.id.dining_center_spinner);
        mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        foodOptionsContainer = findViewById(R.id.food_options_container);


        // Containers for each meal type in "Selected Meals" section
        breakfastContainer = findViewById(R.id.breakfast_container);
        lunchContainer = findViewById(R.id.lunch_container);
        dinnerContainer = findViewById(R.id.dinner_container);
        lateNightContainer = findViewById(R.id.late_night_container);

        selectedMeals = new HashMap<>(); // Initialize the map

        Button generateButton = findViewById(R.id.generate_button);
        generateButton.setOnClickListener(v -> generateFoodOptions());

        fetchUserMealPlan();
    }


    /**
     * Fetches the user's meal plan.
     * This method retrieves the meal plan associated with the user from the backend or database.
     */
    private void fetchUserMealPlan() {
        // Construct the URL for the GET request
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users/" + userId + "/DHmealplans";

        // Create a JsonArrayRequest to fetch meal plans
        JsonArrayRequest mealPlansRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseMealPlans(response); // Handle the response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FoodMenuActivity.this, "Error fetching meal plans.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(mealPlansRequest);
    }

    /**
     * Parses the meal plans returned from the backend.
     * @param mealPlans A JSONArray containing the meal plans to be parsed.
     */
    private void parseMealPlans(JSONArray mealPlans) {
        try {
            // Clear previous selections
            selectedMeals.clear();
            breakfastContainer.removeAllViews();
            lunchContainer.removeAllViews();
            dinnerContainer.removeAllViews();
            lateNightContainer.removeAllViews();

            JSONObject latestMealPlan = null; // To store the latest meal plan

            // Find the latest meal plan based on the highest ID
            for (int i = 0; i < mealPlans.length(); i++) {
                JSONObject mealPlan = mealPlans.getJSONObject(i);
                if (latestMealPlan == null || mealPlan.getInt("id") > latestMealPlan.getInt("id")) {
                    latestMealPlan = mealPlan; // Update the latest meal plan
                    mealID = mealPlan.getInt("id");
                }
            }

            if (latestMealPlan != null) {
                // Access the foodItems array of the latest meal plan
                JSONArray foodItems = latestMealPlan.getJSONArray("foodItems");

                // Iterate through the food items
                for (int j = 0; j < foodItems.length(); j++) {
                    JSONObject food = foodItems.getJSONObject(j);

                    // Extract meal time from the food item
                    String mealTime = food.getString("time"); // Get the meal time from the food item

                    // Extract food properties
                    FoodOption foodOption = new FoodOption(
                            food.getString("name"), // Get the name of the food item
                            food.getInt("calories"),
                            food.getInt("protein"),
                            food.getInt("carbs"),
                            food.getInt("fat"),
                            food.getInt("id")
                    );

                    // Always add the food option to the selectedMeals map
                    selectedMeals.putIfAbsent(mealTime, new ArrayList<>()); // Initialize the list if not present
                    selectedMeals.get(mealTime).add(foodOption); // Add the food option to the corresponding meal time
                }

                // Update UI for each meal time after processing all food items
                for (String mealTime : selectedMeals.keySet()) {
                    updateSelectedMealUI(mealTime); // Update UI for the current meal time
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Generates available food options for the user to choose from.
     * This method creates a list of food options based on the user's preferences or meal plan.
     */
    private void generateFoodOptions() {
        foodOptionsContainer.removeAllViews(); // Clear previous views

        // Get selected dining center and meal type
        String diningHall = diningCenterSpinner.getSelectedItem().toString();
        String mealType = mealTypeSpinner.getSelectedItem().toString();

        // Map spinner values to API path values if necessary
        diningHall = mapDiningHallName(diningHall);

        // Construct the URL
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/Dininghall/" + diningHall + "/" + mealType;

        // Create a JSON request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseFoodItems(response, mealType);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FoodMenuActivity.this, "Error fetching food items.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Parses food items specific to a meal type (e.g., breakfast, lunch).
     * @param foodItems A JSONArray containing the food items to be parsed.
     * @param mealType A String representing the type of meal (e.g., "Breakfast", "Lunch").
     */
    private void parseFoodItems(JSONArray foodItems, String mealType) {
        try {
            for (int i = 0; i < foodItems.length(); i++) {
                JSONObject foodObject = foodItems.getJSONObject(i);

                // Get all necessary fields from the JSON
                int id = foodObject.getInt("id");
                String foodName = foodObject.getString("name");
                int calories = foodObject.getInt("calories");
                int protein = foodObject.getInt("protein");
                int carbs = foodObject.getInt("carbs");
                int fat = foodObject.getInt("fat");

                // Create and display food cards with the API data
                CardView foodCard = createFoodCard(foodName, calories, protein, carbs, fat, mealType, id);
                foodOptionsContainer.addView(foodCard);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a card view to display the details of a food item.
     * @param foodName A String representing the name of the food item.
     * @param calories An integer representing the calorie count of the food item.
     * @param protein An integer representing the protein content in the food item.
     * @param carbs An integer representing the carbohydrate content in the food item.
     * @param fat An integer representing the fat content in the food item.
     * @param mealType A String representing the type of meal (e.g., "Breakfast", "Lunch").
     * @param id An integer representing the ID of the food item.
     * @return A CardView object containing the food item's details.
     */
    private CardView createFoodCard(String foodName, int calories, int protein, int carbs, int fat, String mealType, int id) {
        CardView cardView = new CardView(this);
        cardView.setCardElevation(8);
        cardView.setRadius(12);
        cardView.setContentPadding(16, 16, 16, 16);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView foodNameText = new TextView(this);
        foodNameText.setText("Food: " + foodName);
        foodNameText.setTextSize(18);

        TextView calorieText = new TextView(this);
        calorieText.setText("Calories: " + calories + " kcal");

        TextView proteinText = new TextView(this);
        proteinText.setText("Protein: " + protein + " g");

        TextView carbsText = new TextView(this);
        carbsText.setText("Carbs: " + carbs + " g");

        TextView fatText = new TextView(this);
        fatText.setText("Fat: " + fat + " g");

        // Add all text views to the layout
        layout.addView(foodNameText);
        layout.addView(calorieText);
        layout.addView(proteinText);
        layout.addView(carbsText);
        layout.addView(fatText);

        cardView.addView(layout);

        // Set a click listener to select this food option for the meal type
        cardView.setOnClickListener(v -> selectMealOption(mealType, new FoodOption(foodName, calories, protein, carbs, fat, id)));

        return cardView;
    }


    /**
     * Selects a food option to be added to the user's meal plan.
     * @param mealType A String representing the type of meal (e.g., "Breakfast", "Lunch").
     * @param foodOption A FoodOption object representing the selected food option.
     */
    private void selectMealOption(String mealType, FoodOption foodOption) {
        // Get the current list of selected meals for this meal type, or create a new list if it doesn't exist
        List<FoodOption> selectedMealList = selectedMeals.get(mealType);
        if (selectedMealList == null) {
            selectedMealList = new ArrayList<>();
            selectedMeals.put(mealType, selectedMealList); // Save the new list in the map
        }

        // Add the food option to the list
        selectedMealList.add(foodOption);

        // Call the method to send a POST request to add the selected food option
        sendAddMealRequest(foodOption, mealType);

        // Update the corresponding UI section to reflect the current selection
        updateSelectedMealUI(mealType);
    }


    /**
     * Removes a food item from the user's meal plan.
     * @param foodItemId An integer representing the ID of the food item to be removed.
     */
    private void removeFoodItem(int foodItemId) {
        // Prepare the URL for the DELETE request
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/DHmealplans/" + mealID + "/fooditems/remove/byId";
        // Convert the single food item ID to a string
        String requestBody = String.valueOf(foodItemId); // A single ID as a string

        // Create a PUT request to remove the food item
        StringRequest removeFoodRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RemoveFoodResponse", response);
                        // Handle success response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RemoveFoodError", error.toString());
                        Toast.makeText(FoodMenuActivity.this, "Error removing food item: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                // Log the request body to confirm it's correct
                Log.d("RemoveFoodRequestBody", requestBody);
                return requestBody.getBytes(); // Return the request body as bytes
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(removeFoodRequest);
    }





    /**
     * Adds a food item to the user's meal plan.
     * @param mealPlanID An integer representing the ID of the meal plan to which the food item will be added.
     */
    private void addFoodItem(int mealPlanID) {
        // Prepare the URL for the PUT request
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/DHmealplans/"+mealPlanID+"/fooditems/add/byId";

        // Create a PUT request
        StringRequest addFoodRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AddFoodResponse", response);
                        // Handle success response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AddFoodError", error.toString());
                        Toast.makeText(FoodMenuActivity.this, "Error adding food items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                // Prepare the request body with the food item IDs
                String requestBody = createRequestBodyForFoodItemIds();
                Log.d("AddFoodRequestBody", requestBody);
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(addFoodRequest);
    }


    /**
     * Creates a request body for adding selected food items to the meal plan.
     * @return A String representing the request body for adding food items.
     */
    private String createRequestBodyForFoodItemIds() {
        // Assuming you have a list or collection of food item IDs to add
        List<Integer> foodItemIds = getSelectedFoodItemIds(); // Implement this method to retrieve selected IDs
        return TextUtils.join(",", foodItemIds); // Join the IDs into a comma-separated string
    }

    /**
     * Gets the list of selected food item IDs.
     * @return A List of integers representing the IDs of the selected food items.
     */
    private List<Integer> getSelectedFoodItemIds() {
        List<Integer> ids = new ArrayList<>();

        // Assuming selectedMeals is a map where you keep track of selected food options
        for (List<FoodOption> mealOptions : selectedMeals.values()) {
            for (FoodOption option : mealOptions) {
                ids.add(option.id); // Collecting food item IDs from selected meals
            }
        }
        return ids;
    }

    /**
     * Associates a specific meal plan with the user.
     * @param mealPlanID An integer representing the ID of the meal plan to be associated with the user.
     */
    private void associateMealPlanWithUser(int mealPlanID) {
        // Prepare the URL for the PUT request
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users/" + userId +"/DHmealplan/" +mealPlanID;

        // Create a PUT request
        StringRequest associateRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AssociateMealResponse", response);
                        // Handle response (e.g., show a success message)

                        addFoodItem(mealPlanID);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AssociateMealError", error.toString());
                        Toast.makeText(FoodMenuActivity.this, "Error associating meal plan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                // Prepare the request body with the selected meals
                String requestBody = createRequestBodyForMeals();
                Log.d("AssociateMealRequestBody", requestBody);
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(associateRequest);
    }


    /**
     * Creates the request body for adding meals to the user's meal plan.
     * @return A String representing the request body for adding the meals.
     */
    private String createRequestBodyForMeals() {
        JSONArray foodsArray = new JSONArray();

        // Iterate through selected meals and create JSON objects
        for (Map.Entry<String, List<FoodOption>> entry : selectedMeals.entrySet()) {
            List<FoodOption> mealOptions = entry.getValue();
            for (FoodOption foodOption : mealOptions) {
                JSONObject foodObject = new JSONObject();
                try {
                    foodObject.put("id", foodOption.id);
                    foodObject.put("name", foodOption.getName());
                    foodObject.put("protein", foodOption.protein);
                    foodObject.put("carbs", foodOption.carbs);
                    foodObject.put("fat", foodOption.fat);
                    foodObject.put("calories", foodOption.calories);
                    foodObject.put("dininghall", mapDiningHallName(diningCenterSpinner.getSelectedItem().toString()));
                    foodObject.put("time", entry.getKey()); // The key is the meal type (Breakfast, Lunch, etc.)
                    foodsArray.put(foodObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // Create the final request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("foods", foodsArray);
            requestBody.put("protein", calculateTotalProtein()); // You can implement this method to sum protein
            requestBody.put("carbs", calculateTotalCarbs()); // You can implement this method to sum carbs
            requestBody.put("fat", calculateTotalFat()); // You can implement this method to sum fat
            requestBody.put("finalCalories", calculateTotalCalories()); // You can implement this method to sum calories
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestBody.toString();
    }

    // Helper methods to calculate nutritional totals
    private int calculateTotalProtein() {
        int totalProtein = 0;
        for (List<FoodOption> options : selectedMeals.values()) {
            for (FoodOption option : options) {
                totalProtein += option.protein;
            }
        }
        return totalProtein;
    }

    private int calculateTotalCarbs() {
        int totalCarbs = 0;
        for (List<FoodOption> options : selectedMeals.values()) {
            for (FoodOption option : options) {
                totalCarbs += option.carbs;
            }
        }
        return totalCarbs;
    }

    private int calculateTotalFat() {
        int totalFat = 0;
        for (List<FoodOption> options : selectedMeals.values()) {
            for (FoodOption option : options) {
                totalFat += option.fat;
            }
        }
        return totalFat;
    }

    private int calculateTotalCalories() {
        int totalCalories = 0;
        for (List<FoodOption> options : selectedMeals.values()) {
            for (FoodOption option : options) {
                totalCalories += option.calories;
            }
        }
        return totalCalories;
    }


    /**
     * Sends a request to the backend to add a meal to the user's meal plan.
     * @param foodOption A FoodOption object representing the selected meal option to be added.
     * @param mealType A String representing the type of meal (e.g., "Breakfast", "Lunch").
     */

    private void sendAddMealRequest(FoodOption foodOption, String mealType) {
        StringRequest addMealRequest = new StringRequest(
                Request.Method.POST,
                ADD_MEAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AddMealResponse", response);
                        mealPlanID = Integer.parseInt(response.trim());
                        // Handle response as needed (e.g., show a success message)
                        associateMealPlanWithUser(mealPlanID);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AddMealError", error.toString());
                        Toast.makeText(FoodMenuActivity.this, "Error adding meal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                // Format the request body as required
                String requestBody = String.format(
                        "{\"id\":%d,\"name\":\"%s\",\"protein\":%d,\"carbs\":%d,\"fat\":%d,\"calories\":%d,\"dininghall\":\"%s\",\"time\":\"%s\",\"date\":\"%s\"}",
                        foodOption.id,
                        foodOption.getName(),
                        foodOption.protein,
                        foodOption.carbs,
                        foodOption.fat,
                        foodOption.calories,
                        mapDiningHallName(diningCenterSpinner.getSelectedItem().toString()), // Ensure you map the dining hall name
                        mealType
                );

                Log.d("AddMealRequestBody", requestBody);
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(addMealRequest);
    }

    /**
     * Updates the UI with the selected meal details.
     * @param mealType A String representing the type of meal (e.g., "Breakfast", "Lunch").
     */
    private void updateSelectedMealUI(String mealType) {
        LinearLayout targetContainer;

        switch (mealType) {
            case "Breakfast":
                targetContainer = breakfastContainer;
                break;
            case "Lunch":
                targetContainer = lunchContainer;
                break;
            case "Dinner":
                targetContainer = dinnerContainer;
                break;
            case "Late Night":
                targetContainer = lateNightContainer;
                break;
            default:
                return; // Unknown meal type, do nothing
        }

        targetContainer.removeAllViews(); // Clear previous selection for this meal type

        List<FoodOption> selectedOptions = selectedMeals.get(mealType);
        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            for (FoodOption option : selectedOptions) {
                TextView mealTextView = new TextView(this);
                mealTextView.setText(option.toString());
                mealTextView.setTextSize(16);
                mealTextView.setPadding(10, 10, 10, 10);
                mealTextView.setBackgroundColor(Color.LTGRAY); // Optional: for visual clarity

                // Make the text view clickable
                mealTextView.setOnClickListener(view -> showDeleteConfirmation(option, mealType));

                targetContainer.addView(mealTextView);
            }
        }
    }

    /**
     * Displays a confirmation dialog to the user before removing a food item from a meal plan.
     * @param option A FoodOption object representing the food item to be removed.
     * @param mealType A String representing the type of meal (e.g., "Breakfast", "Lunch").
     * This method shows a confirmation dialog with 'Yes' and 'No' options. If the user selects 'Yes',
     * the food item is removed from the meal plan and the UI is updated accordingly.
     */
    private void showDeleteConfirmation(FoodOption option, String mealType) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Item")
                .setMessage("Are you sure you want to remove this item from " + mealType + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    removeFoodItem(option.id); // Call removeFoodItem with the item ID
                    selectedMeals.get(mealType).remove(option); // Remove item from selected list
                    updateSelectedMealUI(mealType); // Refresh the UI
                })
                .setNegativeButton("No", null)
                .show();
    }




    /**
     * Maps a dining hall name to its corresponding full name or identifier.
     * @param diningHall A String representing the dining hall name to be mapped.
     * @return A String representing the full name or identifier of the dining hall.
     */

    private String mapDiningHallName(String diningHall) {
        switch (diningHall) {
            case "Seasons":
                return "seasons-marketplace-2-2";
            case "Friley":
                return "friley-windows-2-2";
            case "UDCC":
                return "union-drive-marketplace-2-2";
            default:
                return diningHall;
        }
    }


    /**
     * Static inner class representing a food option that can be added to a meal.
     */
    private static class FoodOption {
        private String foodName;
        private int calories;
        private int protein;
        private int carbs;
        private int fat;
        private int id;

        public FoodOption(String foodName, int calories, int protein, int carbs, int fat, int id) {
            this.foodName = foodName;
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
            this.id = id;
        }

        public String getName() {
            return foodName; // Returns the name of the food option
        }

        @Override
        public String toString() {
            return foodName + " (Calories: " + calories + ", Protein: " + protein + "g, Carbs: " + carbs + "g, Fat: " + fat + "g)";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof FoodOption)) return false;
            FoodOption that = (FoodOption) obj;
            return id == that.id; // Consider options equal if they have the same ID
        }

        @Override
        public int hashCode() {
            return id; // Use the ID as the hash code for the FoodOption
        }
    }
}
