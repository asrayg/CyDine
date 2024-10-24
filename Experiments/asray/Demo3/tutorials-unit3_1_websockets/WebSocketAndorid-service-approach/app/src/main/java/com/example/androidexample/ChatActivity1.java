package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatActivity1 extends AppCompatActivity {

    private Button sendBtn, backMainBtn, clearBtn;
    private EditText msgEtx;
    private TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        // Initialize UI elements
        sendBtn = findViewById(R.id.sendBtn);
        backMainBtn = findViewById(R.id.backMainBtn);
        clearBtn = findViewById(R.id.clearBtn);  // Initialize Clear button
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);

        // Set up button listeners
        sendBtn.setOnClickListener(v -> sendMessage());
        backMainBtn.setOnClickListener(v -> navigateToMain());
        clearBtn.setOnClickListener(v -> clearMessages());  // Clear button listener

        // Typing listener
        msgEtx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Broadcast typing status when the user starts typing
                Intent intent = new Intent("SendWebSocketMessage");
                intent.putExtra("key", "chat1");
                intent.putExtra("message", "typing");
                LocalBroadcastManager.getInstance(ChatActivity1.this).sendBroadcast(intent);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void sendMessage() {
        String message = msgEtx.getText().toString();
        if (!message.isEmpty()) {
            Intent intent = new Intent("SendWebSocketMessage");
            intent.putExtra("key", "chat1");
            intent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            // Clear the input field and show a confirmation toast
            msgEtx.setText("");
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void clearMessages() {
        // Clear all the messages in the TextView
        msgTv.setText("");  // Reset the TextView to empty
        Toast.makeText(this, "Chat cleared", Toast.LENGTH_SHORT).show();  // Show a toast notification
    }

    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("chat1".equals(intent.getStringExtra("key"))) {
                String message = intent.getStringExtra("message");
                if (message.startsWith("received:")) {
                    String ackMessage = message.replace("received:", "");
                    runOnUiThread(() -> msgTv.append("\n[Delivered] " + ackMessage));
                } else {
                    String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    runOnUiThread(() -> msgTv.append("\n[" + timestamp + "] " + message));
                }
            }
        }
    };

    private final BroadcastReceiver typingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("chat1".equals(intent.getStringExtra("key"))) {
                boolean isTyping = intent.getBooleanExtra("typing", false);
                if (isTyping) {
                    runOnUiThread(() -> msgTv.append("\nUser is typing..."));
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("WebSocketMessageReceived"));
        LocalBroadcastManager.getInstance(this).registerReceiver(typingReceiver,
                new IntentFilter("WebSocketTypingReceived"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(typingReceiver);
    }
}
