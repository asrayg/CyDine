package com.example.androidexample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 2;

    private ImageView selectedImageView;
    private EditText captionEditText;
    private Button selectImageButton, postButton;
    private TextView feedTextView;
    private Uri selectedImageUri;
    //private WebSocketManager webSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        selectedImageView = findViewById(R.id.selectedImageView);
        captionEditText = findViewById(R.id.captionEditText);
        selectImageButton = findViewById(R.id.selectImageButton);
        postButton = findViewById(R.id.postButton);
        feedTextView = findViewById(R.id.feedTextView);

        // Initialize WebSocketManager and set this as listener
//        webSocketManager = WebSocketManager.getInstance();
//        webSocketManager.setWebSocketListener(this);
//        webSocketManager.connectWebSocket("ws://coms-3090-020.class.las.iastate.edu:8080/chat/ss");

//        selectImageButton.setOnClickListener(v -> openImageChooser());

//        postButton.setOnClickListener(v -> {
//            if (selectedImageUri != null) {
//                convertImageToBase64AndUpload(selectedImageUri);
//            } else {
//                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            selectedImageUri = data.getData();
//            selectedImageView.setImageURI(selectedImageUri); // Display selected image
//        }
//    }
//
//    private void convertImageToBase64AndUpload(Uri imageUri) {
//        try {
//            // Convert image to Base64
//            InputStream inputStream = getContentResolver().openInputStream(imageUri);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, len);
//            }
//            String base64Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//
//            // Send Base64 image string to server
//            uploadImageToServer(base64Image);
//
//            Log.d("CreatePostActivity", "Image Base64 conversion complete.");
//        } catch (FileNotFoundException e) {
//            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
//            Log.e("CreatePostActivity", "Image not found", e);
//        } catch (Exception e) {
//            Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show();
//            Log.e("CreatePostActivity", "Error processing image", e);
//        }
//    }
//
//    private void uploadImageToServer(String base64Image) {
//        String serverUrl = "http://coms-3090-020.class.las.iastate.edu:8080/images";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
//                response -> {
//                    Log.d("CreatePostActivity", "Server response: " + response);
//                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
//
//                    // Send message through WebSocket after successful upload
//                    String caption = captionEditText.getText().toString();
//                    String postData = "{ \"imageUrl\": \"" + base64Image + "\", \"caption\": \"" + caption + "\" }";
//                    webSocketManager.sendMessage(postData);
//
//                },
//                error -> {
//                    Log.e("CreatePostActivity", "Error uploading image: " + error.getMessage());
//                    Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show();
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("image", base64Image);
//                return params;
//            }
//        };
//
//        Volley.newRequestQueue(this).add(stringRequest);
//    }
//
//    // WebSocket event handlers
//    @Override
//    public void onWebSocketMessage(String message) {
//        Log.d("WebSocket", "Received message: " + message);
//        runOnUiThread(() -> {
//            String currentText = feedTextView.getText().toString();
//            feedTextView.setText(currentText + "\n" + message);
//        });
//    }
//
//    @Override
//    public void onWebSocketClose(int code, String reason, boolean remote) {
//        Log.d("WebSocket", "Connection closed. Code: " + code + ", Reason: " + reason);
//        if (!remote) {
//            Log.d("WebSocket", "Reconnecting...");
//            webSocketManager.connectWebSocket("ws://coms-3090-020.class.las.iastate.edu:8080/chat/ss");
//        }
//    }
//
//    @Override
//    public void onWebSocketOpen(ServerHandshake handshakedata) {
//        Log.d("WebSocket", "WebSocket opened: " + handshakedata.getHttpStatus());
//    }
//
//    @Override
//    public void onWebSocketError(Exception ex) {
//        Log.e("WebSocket", "WebSocket error: " + ex.getMessage());
//    }

}
