package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity1 extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn, backMainBtn;
    private EditText msgEtx;
    private TextView msgTv, messageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        // Initialize UI elements
        sendBtn = findViewById(R.id.sendBtn);
        backMainBtn = findViewById(R.id.backMainBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);
        messageStatus = findViewById(R.id.messageStatus);

        // Connect to WebSocket
        WebSocketManager1.getInstance().setWebSocketListener(this);

        // Send button listener
        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString().trim();
            if (message.isEmpty()) {
                msgEtx.setError("Message cannot be empty");
                return; // Prevent sending empty messages
            }
            try {
                WebSocketManager1.getInstance().sendMessage(message);
                messageStatus.setText("Delivered"); // Set status to "Delivered"
                msgEtx.setText(""); // Clear input field
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
        });

        // Back button listener
        backMainBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String existingMessages = msgTv.getText().toString();
            msgTv.setText(existingMessages + "\n" + message);
            messageStatus.setText("Read"); // Set status to "Read" when a message is received
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
