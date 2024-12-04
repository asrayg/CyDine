package com.example.androidexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * feed activity class for posting
 */
//

//
public class FeedActivity extends AppCompatActivity implements WebSocketListener {

    private static final List<String> messageCache = new ArrayList<>(); // Cache for messages
    private static final String USER_DETAILS_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";
    private static final List<String> imageCache = new ArrayList<>();
    private final Map<String, List<String>> commentCache = new HashMap<>();
    private TextView feedTextView;
    private WebSocketManager webSocketManager;
    private Button uploadImageButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private RecyclerView feedRecyclerView;
    private ImageAdapter imageAdapter;
    private boolean counter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize UI components
        feedTextView = findViewById(R.id.feedTextView);
        uploadImageButton = findViewById(R.id.uploadImageButton);

        // Set up RecyclerView and adapter for feed
        feedRecyclerView = findViewById(R.id.feedRecyclerView);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new ImageAdapter(this);
        feedRecyclerView.setAdapter(imageAdapter);
        counter = false;

        String userId = getIntent().getStringExtra("userId");
        fetchUserDetails(userId);

        restoreMessages();

        // Set up the button to open image picker
        uploadImageButton.setOnClickListener(v -> openImagePicker());
    }

    private void fetchUserDetails(String userId) {
        String userUrl = USER_DETAILS_URL + "/" + userId;

        StringRequest userRequest = new StringRequest(
                Request.Method.GET,
                userUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            userName = jsonObject.getString("name");

                            // Initialize WebSocket only after the userName is fetched
                            initializeWebSocket();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedActivity.this, "Error parsing user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UserDetailsError", error.toString());
                        Toast.makeText(FeedActivity.this, "Error fetching user details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(userRequest);
    }

    private void initializeWebSocket() {
        String serverUrl = "ws://coms-3090-020.class.las.iastate.edu:8080/chat/" + userName;
        Log.d("WebSocket", "Connecting to WebSocket: " + serverUrl);

        webSocketManager = WebSocketManager.getInstance();
        webSocketManager.setWebSocketListener(this);
        webSocketManager.connectWebSocket(serverUrl);
    }



    private void restoreMessages() {
        // Restore text messages in TextView
        StringBuilder allMessages = new StringBuilder();
        for (String message : messageCache) {
            allMessages.append(message).append("\n");
        }
        feedTextView.setText(allMessages.toString());
        // Restore image URLs and their comments in RecyclerView
        for (int i = 0; i < imageCache.size(); i++) {
            String imageUrl = imageCache.get(i);
            imageAdapter.addImage(imageUrl);

            // Restore associated comments for this image
            if (commentCache.containsKey(imageUrl)) { // Check if there are comments cached for this image
                Log.d("FeedActivity", "Yes!!! ");
                List<String> comments = commentCache.get(imageUrl);
                for (String comment : comments) {
                    imageAdapter.addCommentToImage(imageUrl, comment);
                }
            }
        }
    }


    // Method to open image picker for selecting an image
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Automatically upload the image after selection
            uploadImage();
        }
    }

    // Method to upload the selected image to the server
    private void uploadImage() {
        counter = true;
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

    // Fetch uploaded image from server
    private void fetchUploadedImage(String imageName) {
        String imageUrl = "http://coms-3090-020.class.las.iastate.edu:8080/images/gets?image=" + imageName;
        Log.d("FeedActivity", "Fetching image from URL: " + imageUrl);

        // Add image to RecyclerView without removing old images
        imageAdapter.addImage(imageUrl);
        Toast.makeText(FeedActivity.this, "Image fetched and displayed!", Toast.LENGTH_SHORT).show();
    }

    // Get file path from URI
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

    // Convert URI to byte array (file data)
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
        runOnUiThread(() -> {
            if (message.contains("#")) {
                // Comment message
                if(counter) {
                    int firstColonIndex = message.indexOf(":");
                    if (firstColonIndex == -1) {
                        Log.e("FeedActivity", "Invalid message format: " + message);
                        return;
                    }

                    // Extract the username
                    String username = message.substring(0, firstColonIndex).trim();

                    // Extract the rest of the message
                    String content = message.substring(firstColonIndex + 1).trim();

                    if (content.startsWith("#")) {
                        content = content.substring(1); // Remove the "#" identifier
                    }

                    // Find the last colon to isolate the comment
                    int lastColonIndex = content.lastIndexOf(":");
                    if (lastColonIndex == -1) {
                        Log.e("FeedActivity", "Invalid content format: " + content);
                        return;
                    }

                    // Extract the image URL and the comment
                    String imageUrl = content.substring(0, lastColonIndex).trim();
                    String comment = content.substring(lastColonIndex + 1).trim();

                    Log.d("FeedActivity", "Username: " + username);
                    Log.d("FeedActivity", "Image URL: " + imageUrl);
                    Log.d("FeedActivity", "Comment: " + comment);

                    // Update comment in the adapter
                    imageAdapter.addCommentToImage(imageUrl, comment);
                    if (!commentCache.containsKey(imageUrl)) {
                        commentCache.put(imageUrl, new ArrayList<>());
                    }

                    commentCache.get(imageUrl).add(comment);
                    messageCache.add(comment);
                }
            } else if (message.contains("@")) {
                // Image upload message
                int atIndex = message.indexOf("@");
                String imageName = message.substring(atIndex + 1).trim();
                String imageUrl = "http://coms-3090-020.class.las.iastate.edu:8080/images/gets?image=" + imageName;

                if(counter){
                    imageCache.add(imageUrl);
                    fetchUploadedImage(imageName);
                }
            } else {
                Log.d("FeedActivity", "its here now: " + message);
                messageCache.add(message);
                //feedTextView.setText(feedTextView.getText().toString() + "\n" + message);
            }
        });
    }


    // WebSocket callback for connection closure
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Handle WebSocket connection closure
        Log.d("WebSocket", "Connection closed. Code: " + code + ", Reason: " + reason);
    }

    // WebSocket callback for connection opening
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Handle WebSocket connection opened
        Log.d("WebSocket", "WebSocket opened: " + handshakedata.getHttpStatus());
    }

    // WebSocket callback for error handling
    @Override
    public void onWebSocketError(Exception ex) {
        // Handle WebSocket error
        Log.e("WebSocket", "WebSocket error: " + ex.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect WebSocket when the activity is destroyed
        if (webSocketManager != null) {
            webSocketManager.disconnectWebSocket();
            Log.d("WebSocket", "WebSocket disconnected in onDestroy.");
        }
    }
}
