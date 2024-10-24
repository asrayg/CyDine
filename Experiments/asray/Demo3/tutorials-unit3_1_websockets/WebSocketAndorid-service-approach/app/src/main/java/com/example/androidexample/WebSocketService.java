package com.example.androidexample;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WebSocketService extends Service {

    private final Map<String, WebSocketClient> webSockets = new HashMap<>();

    public WebSocketService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("CONNECT".equals(action)) {
                String url = intent.getStringExtra("url");
                String key = intent.getStringExtra("key");
                connectWebSocket(key, url); // Initialize WebSocket connection
            } else if ("DISCONNECT".equals(action)) {
                String key = intent.getStringExtra("key");
                disconnectWebSocket(key);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("SendWebSocketMessage"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (WebSocketClient client : webSockets.values()) {
            client.close();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Initialize WebSocket connection
    private void connectWebSocket(String key, String url) {
        WebSocketClient webSocketClient = null;

        try {
            URI serverUri = URI.create(url);
            WebSocketClient finalWebSocketClient = webSocketClient;
            webSocketClient = new WebSocketClient(serverUri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d(key, "Connected");
                }

                @Override
                public void onMessage(String message) {
                    if ("typing".equals(message)) {
                        Intent typingIntent = new Intent("WebSocketTypingReceived");
                        typingIntent.putExtra("key", key);
                        typingIntent.putExtra("typing", true);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(typingIntent);
                    } else {
                        Intent intent = new Intent("WebSocketMessageReceived");
                        intent.putExtra("key", key);
                        intent.putExtra("message", message);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                        // Send acknowledgment
                        if (finalWebSocketClient != null) {
                            finalWebSocketClient.send("received:" + message);
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(key, "Closed");
                    reconnectWebSocket(key, url);
                }

                @Override
                public void onError(Exception ex) {
                    Log.d(key, "Error");
                }
            };

            webSocketClient.connect();
            webSockets.put(key, webSocketClient); // add WebSocketClient to the map

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reconnection logic
    private void reconnectWebSocket(String key, String url) {
        new Handler().postDelayed(() -> connectWebSocket(key, url), 5000); // Retry after 5 seconds
    }

    // Handle incoming messages
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            String message = intent.getStringExtra("message");

            WebSocketClient webSocket = webSockets.get(key);
            if (webSocket != null) {
                webSocket.send(message);
            }
        }
    };

    // Disconnect WebSocket
    private void disconnectWebSocket(String key) {
        if (webSockets.containsKey(key))
            webSockets.get(key).close();
    }
}
