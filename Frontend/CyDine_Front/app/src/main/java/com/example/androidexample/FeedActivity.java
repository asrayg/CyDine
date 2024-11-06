package com.example.androidexample;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity implements WebSocketListener {

    private Button createPostButton;
    private TextView feedTextView;
    private WebSocketManager webSocketManager;
    private AppCompatEditText messageEditText; // Reference to the EditText
    private Button uploadImageButton;
    private ImageView selectedImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    private RecyclerView feedRecyclerView;
    private ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize UI components
        createPostButton = findViewById(R.id.createPostButton);
        feedTextView = findViewById(R.id.feedTextView);
        messageEditText = findViewById(R.id.messageEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        selectedImageView = findViewById(R.id.selectedImageView);

        feedRecyclerView = findViewById(R.id.feedRecyclerView);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new ImageAdapter(this);
        feedRecyclerView.setAdapter(imageAdapter);

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

        uploadImageButton = findViewById(R.id.uploadImageButton);
        selectedImageView = findViewById(R.id.selectedImageView);

        // Set up the button to open image picker
        // Handle uploadImageButton click
        uploadImageButton.setOnClickListener(v -> openImagePicker());


    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri); // Display selected image

            // Automatically upload the image after selection
            uploadImage();
        }
    }

    private void uploadImage() {
        if (selectedImageUri == null) {
            Log.e("FeedActivity", "No image selected for upload.");
            return;
        }

        String serverUrl = "http://coms-3090-020.class.las.iastate.edu:8080/images"; // Replace with your server URL

        // Convert the image URI to a File object
        File file = new File(getPathFromURI(selectedImageUri));

        // Create a custom request for file upload
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, serverUrl,
                response -> {
                    // Log success and show toast
                    Log.d("FeedActivity", "Image uploaded successfully!");
                    runOnUiThread(() -> Toast.makeText(FeedActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show());
                    webSocketManager.sendMessage("@" + file.getName());
                },
                error -> {
                    // Log error and show toast
                    Log.e("FeedActivity", "Image upload failed: " + error.getMessage());
                    runOnUiThread(() -> Toast.makeText(FeedActivity.this, "Image upload failed!", Toast.LENGTH_SHORT).show());
                }
        ) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    byte[] fileData = getFileDataFromUri(selectedImageUri);
                    params.put("image", new DataPart(file.getName(), fileData, "image/jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(multipartRequest);
    }

    private void fetchUploadedImage(String imageName) {
        String imageUrl = "http://coms-3090-020.class.las.iastate.edu:8080/images/gets?image=" + imageName;
        Log.d("FeedActivity", "Fetching image from URL: " + imageUrl);


            imageAdapter.addImage(imageUrl); // This adds the new image without removing old ones
            Toast.makeText(FeedActivity.this, "Image fetched and displayed!", Toast.LENGTH_SHORT).show();

    }




    private String getPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    private byte[] getFileDataFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }





    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Received message: " + message);

        // Use runOnUiThread to ensure UI update happens on the main thread
        runOnUiThread(() -> {
            // Check if the message starts with "@" (indicating an image name)
            if (message.startsWith("@")) {
                String imageName = message.substring(1);  // Remove the "@" prefix
                fetchUploadedImage(imageName);            // Fetch and display the image
                Log.d("WebSocket", "Received message: " + imageName);
            } else {
                // Handle other types of messages, if any
                String currentText = feedTextView.getText().toString();
                feedTextView.setText("\n" + message);
            }
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