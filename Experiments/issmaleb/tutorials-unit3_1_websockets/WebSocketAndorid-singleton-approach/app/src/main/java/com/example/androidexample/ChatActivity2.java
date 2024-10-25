package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity2 extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn2, backMainBtn2;
    private EditText msgEtx2;
    private TextView msgTv2, messageStatus2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        // Initialize UI elements
        sendBtn2 = findViewById(R.id.sendBtn2);
        msgEtx2 = findViewById(R.id.msgEdt2);
        msgTv2 = findViewById(R.id.tx2);
        messageStatus2 = findViewById(R.id.messageStatus2);
        
        // Connect to WebSocket
        WebSocketManager2.getInstance().setWebSocketListener(this);

        // Send button listener
        sendBtn2.setOnClickListener(v -> {
            String message = msgEtx2.getText().toString().trim();
            if (message.isEmpty()) {
                msgEtx2.setError("Message cannot be empty");
                return; // Prevent sending empty messages
            }
            try {
                WebSocketManager2.getInstance().sendMessage(message);
                messageStatus2.setText("Delivered"); // Set status to "Delivered"
                msgEtx2.setText(""); // Clear input field
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
        });

        // Back button listener
        backMainBtn2.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String existingMessages = msgTv2.getText().toString();
            msgTv2.setText(existingMessages + "\n" + message);
            messageStatus2.setText("Read"); // Set status to "Read" when a message is received
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("WebSocketClose", "Code: " + code + ", Reason: " + reason);
        // Handle connection close (optional)
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("WebSocketOpen", "Connection established");
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("WebSocketError", "Error: " + ex.getMessage());
    }
}
