package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class MessageActivity extends AppCompatActivity {
    private WebSocketClient webSocketClient;
    private EditText messageInput;
    private EditText mealPlanIdInput;
    private LinearLayout messageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_chat);

        messageInput = findViewById(R.id.messageInput);
        mealPlanIdInput = findViewById(R.id.mealPlanIdInput);
        messageContainer = findViewById(R.id.messageContainer);

        Button sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // Replace "22" with actual user ID
        connectWebSocket("ws://coms-3090-020.class.las.iastate.edu:8080/mpchat/22");
    }

    private void connectWebSocket(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                runOnUiThread(() -> displayMessage("Connected to server"));
            }

            @Override
            public void onMessage(String message) {
                runOnUiThread(() -> displayMessage(message));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                runOnUiThread(() -> displayMessage("Disconnected from server"));
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                runOnUiThread(() -> displayMessage("Error: " + ex.getMessage()));
            }
        };

        webSocketClient.connect();
    }

    private void sendMessage() {
        String userId = "22";  // replace with actual user ID if available
        String messageText = messageInput.getText().toString();
        String mealPlanIdText = mealPlanIdInput.getText().toString();

        if (messageText.isEmpty() || mealPlanIdText.isEmpty()) {
            displayMessage("Message and Meal Plan ID cannot be empty.");
            return;
        }

        int mealPlanId = Integer.parseInt(mealPlanIdText);

        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("userId", userId);
            messageJson.put("message", messageText);
            messageJson.put("mealplanId", mealPlanId);

            webSocketClient.send(messageJson.toString());

            messageInput.setText("");
            mealPlanIdInput.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String message) {
        TextView textView = new TextView(this);
        textView.setText(message);
        messageContainer.addView(textView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
