package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Activity for managing messages and meal plans using WebSocket and REST APIs.
 * Users can send messages linked to selected meal plans, view live messages, and report inappropriate content.
 */
public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    private WebSocketClient webSocketClient;
    private EditText messageInput;
    private LinearLayout messageContainer;
    private RequestQueue requestQueue;
    private String userId = "1";  // Replace this with the actual user ID

    private int selectedMealPlanId = -1;

    /**
     * Initializes the activity, connects to WebSocket, and sets up UI components.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Null otherwise.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_chat);

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "User ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        messageInput = findViewById(R.id.messageInput);
        messageContainer = findViewById(R.id.messageContainer);
        requestQueue = Volley.newRequestQueue(this);

        Button chooseMealPlanButton = findViewById(R.id.chooseMealPlanButton);
        Button sendMessageButton = findViewById(R.id.sendMessageButton);

        // Choose Meal Plan Button
        chooseMealPlanButton.setOnClickListener(view -> fetchAndShowMealPlans(userId));  // Replace "1" with the actual user ID

        // Send Message Button
        sendMessageButton.setOnClickListener(view -> sendMessage());

        connectWebSocket("ws://coms-3090-020.class.las.iastate.edu:8080/mpchat/"+userId);
    }

    /**
     * Establishes a WebSocket connection.
     *
     * @param url The WebSocket server URL.
     */
    private void connectWebSocket(String url) {
        try {
            URI uri = new URI(url);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d(TAG, "WebSocket Connected");
                    runOnUiThread(() -> displayMessage("Connected to server", "System", -1));
                }

                @Override
                public void onMessage(String message) {
                    Log.d(TAG, "Message received: " + message);
                    runOnUiThread(() -> handleIncomingMessage(message));
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(TAG, "WebSocket Disconnected: " + reason);
                    runOnUiThread(() -> displayMessage("Disconnected from server", "System", -1));
                }

                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "WebSocket Error: " + ex.getMessage(), ex);
                    runOnUiThread(() -> displayMessage("Error: " + ex.getMessage(), "System", -1));
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "Invalid WebSocket URI", e);
        }
    }

    /**
     * Sends a message through the WebSocket connection.
     * The message is linked to the selected meal plan.
     */
    private void sendMessage() {
        if (selectedMealPlanId == -1) {
            Toast.makeText(this, "Please select a meal plan first.", Toast.LENGTH_SHORT).show();
            return;
        }

        String messageText = messageInput.getText().toString();

        if (messageText.isEmpty()) {
            displayMessage("Message cannot be empty.", "System", -1);
            return;
        }

        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("userId", userId);
            messageJson.put("message", messageText);
            messageJson.put("mealplanId", selectedMealPlanId);

            Log.d(TAG, "Sending message: " + messageJson.toString());
            webSocketClient.send(messageJson.toString());

            messageInput.setText("");
        } catch (Exception e) {
            Log.e(TAG, "Error creating message JSON", e);
        }
    }

    private void fetchUsername(String userId, String message, int mealPlanId) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users/" + userId;
        Log.d(TAG, "Fetching username for userId: " + userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String username = response.getString("name");
                        Log.d(TAG, "Fetched username: " + username);
                        displayMessage(message, username, mealPlanId);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing username from response", e);
                        displayMessage(message, "Unknown User", mealPlanId);
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching username", error);
                    displayMessage(message, "Unknown User", mealPlanId);
                });

        requestQueue.add(request);
    }

    /**
     * Handles incoming messages by fetching the username and displaying the message.
     *
     * @param message The incoming JSON-formatted message.
     */
    private void handleIncomingMessage(String message) {
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String userId = jsonMessage.getString("userId");
            String content = jsonMessage.getString("message");
            int mealPlanId = jsonMessage.getInt("mealplanId");

            Log.d(TAG, "Processing message from user ID: " + userId);
            fetchUsername(userId, content, mealPlanId);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing incoming message JSON", e);
        }
    }

    /**
     * Fetches available meal plans for the user and displays a selection dialog.
     *
     * @param userId The user's ID.
     */
    private void fetchAndShowMealPlans(String userId) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users/" + userId + "/mealplans";
        Log.d(TAG, "Fetching meal plans for userId: " + userId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> showMealPlanSelectionDialog(response),
                error -> Log.e(TAG, "Error fetching meal plans", error));

        requestQueue.add(request);
    }

    /**
     * Displays a dialog for selecting a meal plan from the available options.
     *
     * @param mealPlans The JSON array of meal plans.
     */
    private void showMealPlanSelectionDialog(JSONArray mealPlans) {
        ArrayList<String> mealPlanDescriptions = new ArrayList<>();
        ArrayList<Integer> mealPlanIds = new ArrayList<>();

        // Parse meal plans
        for (int i = 0; i < mealPlans.length(); i++) {
            try {
                JSONObject mealPlan = mealPlans.getJSONObject(i);
                int mealPlanId = mealPlan.getInt("id");
                JSONArray foodItems = mealPlan.getJSONArray("foodItems");

                StringBuilder description = new StringBuilder("MealPlan ID: " + mealPlanId + "\nItems: ");
                for (int j = 0; j < foodItems.length(); j++) {
                    JSONObject foodItem = foodItems.getJSONObject(j);
                    description.append(foodItem.getString("name"));
                    if (j < foodItems.length() - 1) {
                        description.append(", ");
                    }
                }

                mealPlanDescriptions.add(description.toString());
                mealPlanIds.add(mealPlanId);

            } catch (Exception e) {
                Log.e(TAG, "Error parsing meal plan JSON", e);
            }
        }

        // Show dialog for selection
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Meal Plan")
                .setItems(mealPlanDescriptions.toArray(new String[0]), (dialog, which) -> {
                    selectedMealPlanId = mealPlanIds.get(which);
                    Toast.makeText(this, "Selected Meal Plan ID: " + selectedMealPlanId, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void displayMessage(String message, String username, int mealPlanId) {
        Log.d(TAG, "Displaying message from: " + username);
        if ("System".equals(username) && ("Connected to server".equals(message) || "Disconnected from server".equals(message))) {
            return;
        }

        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.VERTICAL);
        messageLayout.setPadding(10, 10, 10, 10);
        messageLayout.setBackgroundResource(R.drawable.message_background); // Custom background for messages

        // Set margin for spacing between messages
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8); // Adjust top and bottom margin to add spacing
        messageLayout.setLayoutParams(params);


        TextView nameView = new TextView(this);
        nameView.setText(username);
        nameView.setTypeface(null, android.graphics.Typeface.BOLD);

        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2  // Line height in pixels (adjust as needed)
        ));
        separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Set your preferred line color


        TextView messageView = new TextView(this);
        messageView.setText(message);

        TextView reportView = new TextView(this);
        reportView.setText("Report");
        reportView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        reportView.setPadding(0, 8, 0, 0);
        reportView.setTextSize(12);
        reportView.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

        // Set click listener for report action
        reportView.setOnClickListener(v -> reportUser(userId));


        messageLayout.addView(nameView);
        messageLayout.addView(separator);  // Add the separator line
        messageLayout.addView(messageView);
        messageLayout.addView(reportView); // Add the report button

        if (mealPlanId > 0) {
            fetchAndDisplayFoodItems(mealPlanId, messageLayout);
        }


        messageContainer.addView(messageLayout);
    }

    private void reportUser(String userId) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users/" + userId + "/warn";
        Log.d(TAG, "Reporting user: " + userId);

        JsonObjectRequest reportRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                response -> Toast.makeText(this, "User reported successfully", Toast.LENGTH_SHORT).show(),
                error -> {
                    Log.e(TAG, "Error reporting user", error);
                    Toast.makeText(this, "Failed to report user", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(reportRequest);
    }

    /**
     * Fetches and displays food items for a selected meal plan.
     *
     * @param mealPlanId     The selected meal plan ID.
     * @param messageLayout The layout where the food items will be displayed.
     */
    private void fetchAndDisplayFoodItems(int mealPlanId, LinearLayout messageLayout) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/mealplans/" + mealPlanId;
        Log.d(TAG, "Fetching food items for mealPlanId: " + mealPlanId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray foodItems = response.getJSONArray("foodItems");
                        StringBuilder foodItemsList = new StringBuilder("My meal plan is: ");

                        for (int i = 0; i < foodItems.length(); i++) {
                            JSONObject foodItem = foodItems.getJSONObject(i);
                            foodItemsList.append(foodItem.getString("name"));
                            if (i < foodItems.length() - 1) {
                                foodItemsList.append(", ");
                            }
                        }

                        TextView foodItemsView = new TextView(this);
                        foodItemsView.setText(foodItemsList.toString());
                        messageLayout.addView(foodItemsView);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing food items", e);
                    }
                },
                error -> Log.e(TAG, "Error fetching food items", error));

        requestQueue.add(request);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        requestQueue.stop();
    }
}
