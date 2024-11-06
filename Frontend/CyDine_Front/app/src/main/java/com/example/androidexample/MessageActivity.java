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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    private WebSocketClient webSocketClient;
    private EditText messageInput;
    private EditText mealPlanIdInput;
    private LinearLayout messageContainer;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_chat);

        messageInput = findViewById(R.id.messageInput);
        mealPlanIdInput = findViewById(R.id.mealPlanIdInput);
        messageContainer = findViewById(R.id.messageContainer);
        requestQueue = Volley.newRequestQueue(this);

        Button sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(view -> sendMessage());

        connectWebSocket("ws://coms-3090-020.class.las.iastate.edu:8080/mpchat/1");
    }

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

    private void sendMessage() {
        String userId = "1";  // replace with actual user ID if available
        String messageText = messageInput.getText().toString();
        String mealPlanIdText = mealPlanIdInput.getText().toString();

        if (messageText.isEmpty() || mealPlanIdText.isEmpty()) {
            displayMessage("Message and Meal Plan ID cannot be empty.", "System", -1);
            return;
        }

        int mealPlanId = Integer.parseInt(mealPlanIdText);
        JSONObject messageJson = new JSONObject();

        try {
            messageJson.put("userId", userId);
            messageJson.put("message", messageText);
            messageJson.put("mealplanId", mealPlanId);

            Log.d(TAG, "Sending message: " + messageJson.toString());
            webSocketClient.send(messageJson.toString());

            messageInput.setText("");
            mealPlanIdInput.setText("");
        } catch (Exception e) {
            Log.e(TAG, "Error creating message JSON", e);
        }
    }

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

    private void displayMessage(String message, String username, int mealPlanId) {
        Log.d(TAG, "Displaying message from: " + username);
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.VERTICAL);
        messageLayout.setPadding(10, 10, 10, 10);
        messageLayout.setBackgroundResource(R.drawable.message_background); // Custom background for messages

        TextView nameView = new TextView(this);
        nameView.setText("Name: " + username);
        nameView.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView messageView = new TextView(this);
        messageView.setText("Message: " + message);

        messageLayout.addView(nameView);
        messageLayout.addView(messageView);

        if (mealPlanId > 0) {
            TextView mealPlanView = new TextView(this);
            mealPlanView.setText("Mealplan: " + mealPlanId);
            messageLayout.addView(mealPlanView);
        }

        messageContainer.addView(messageLayout);
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
