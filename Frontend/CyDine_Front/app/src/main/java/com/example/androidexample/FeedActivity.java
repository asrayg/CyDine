package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import org.java_websocket.handshake.ServerHandshake;

public class FeedActivity extends AppCompatActivity implements WebSocketListener {

    private Button createPostButton;
    private TextView feedTextView;
    private WebSocketManager webSocketManager;
    private AppCompatEditText messageEditText; // Reference to the EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize UI components
        createPostButton = findViewById(R.id.createPostButton);
        feedTextView = findViewById(R.id.feedTextView); // Make sure you initialize feedTextView here
        messageEditText = findViewById(R.id.messageEditText); // Initialize the EditText

        // Initialize WebSocketManager and set the listener
        webSocketManager = WebSocketManager.getInstance();

        // WebSocket server URL (use the correct URL)
        String serverUrl = "ws://coms-3090-020.class.las.iastate.edu:8080/chat/ss";
        Log.d("WebSocket", "Connecting to WebSocket: " + serverUrl);

        // Set the WebSocketListener
        webSocketManager.setWebSocketListener(this);

        // Initiate WebSocket connection
        webSocketManager.connectWebSocket(serverUrl);

        // Handle button click to send message
        createPostButton.setOnClickListener(v -> {
            // Get the message from the EditText
            String message = messageEditText.getText().toString();

            // Check if the message is not empty
            if (!message.isEmpty()) {
                // Send the message over WebSocket
                webSocketManager.sendMessage(message);

                // Clear the EditText after sending the message
                messageEditText.setText("");
            }
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        // Update the UI to display the received WebSocket message
        Log.d("WebSocket", "Received message: " + message);

        // Use runOnUiThread to ensure UI update happens on the main thread
        runOnUiThread(() -> {
            // Append the received message to the TextView (you can add new lines or formatting if needed)
            String currentText = feedTextView.getText().toString();
            feedTextView.setText(currentText + "\n" + message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Handle WebSocket connection closure
        Log.d("WebSocket", "Connection closed. Code: " + code + ", Reason: " + reason);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Handle WebSocket connection opened
        Log.d("WebSocket", "WebSocket opened: " + handshakedata.getHttpStatus());
    }

    @Override
    public void onWebSocketError(Exception ex) {
        // Handle WebSocket error
        Log.e("WebSocket", "WebSocket error: " + ex.getMessage());
    }
}
