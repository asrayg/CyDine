package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.java_websocket.handshake.ServerHandshake;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn, emojiBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private LinearLayout emojiPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendBtn = findViewById(R.id.sendBtn);
        emojiBtn = findViewById(R.id.emojiBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);
        emojiPicker = findViewById(R.id.emojiPicker);

        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString().trim();
            if (!message.isEmpty()) {
                try {
                    WebSocketManager.getInstance().sendMessage(message);
                    msgEtx.setText("");
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage());
                }
            }
        });

        emojiBtn.setOnClickListener(v -> {
            if (emojiPicker.getVisibility() == View.GONE) {
                emojiPicker.setVisibility(View.VISIBLE);
            } else {
                emojiPicker.setVisibility(View.GONE);
            }
        });
    }

    public void onEmojiSelected(View view) {
        Button emojiButton = (Button) view;
        String emoji = emojiButton.getText().toString();
        msgEtx.setText(msgEtx.getText().toString() + emoji);
        emojiPicker.setVisibility(View.GONE);
    }

    @Override
    public void onWebSocketMessage(String message) {
        String timestamp = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n" + timestamp + ": " + message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("WebSocketError", ex.getMessage());
    }
}
