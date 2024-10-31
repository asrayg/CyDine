package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv, typingIndicator;
    private SimpleDateFormat timestampFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize UI elements
        sendBtn = findViewById(R.id.sendBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);
        typingIndicator = findViewById(R.id.typingIndicator);

        // Connect this activity to the websocket instance
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        // Set up the timestamp format
        timestampFormat = new SimpleDateFormat("HH:mm:ss");

        // Send button listener
        sendBtn.setOnClickListener(v -> {
            try {
                String message = msgEtx.getText().toString();
                if (!message.trim().isEmpty()) {
                    // Send message via WebSocket
                    WebSocketManager.getInstance().sendMessage(message);
                    msgEtx.setText(""); // Clear input
                }
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
        });

//        // Typing indicator
//        msgEtx.addTextChangedListener(new SimpleTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                WebSocketManager.getInstance().sendMessage("User is typing...");
//                typingIndicator.setText("User is typing...");
//            }
//        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        // Handle typing indicator
        if (message.contains("typing")) {
            runOnUiThread(() -> typingIndicator.setText(message));
            return;
        }

        // In Android, all UI updates must be performed on the main UI thread
        runOnUiThread(() -> {
            // Get timestamp for the message
            String timestamp = timestampFormat.format(new Date());
            String currentText = msgTv.getText().toString();
            msgTv.setText(currentText + "\n" + timestamp + " - " + message);
            typingIndicator.setText(""); // Clear typing indicator
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}
}
