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

/**
 * Activity for creating a post that includes an image and a caption.
 * Users can select an image, enter a caption, and submit the post.
 */

public class CreatePostActivity extends AppCompatActivity {

    // Constant for identifying the image selection request
    private static final int IMAGE_REQUEST_CODE = 2;

    // UI components
    private ImageView selectedImageView; // Displays the selected image
    private EditText captionEditText; // Input field for the post caption
    private Button selectImageButton, postButton; // Buttons for selecting an image and submitting the post
    private TextView feedTextView; // Placeholder text for displaying feed updates (optional)

    // URI of the selected image
    private Uri selectedImageUri;

    // Uncomment if WebSocket functionality is added in the future
    // private WebSocketManager webSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post); // Set the layout for this activity

        // Initialize UI components by linking them to their XML counterparts
        selectedImageView = findViewById(R.id.selectedImageView); // ImageView for displaying the selected image
        captionEditText = findViewById(R.id.captionEditText); // EditText for inputting the caption
        selectImageButton = findViewById(R.id.selectImageButton); // Button for opening the image selector
        postButton = findViewById(R.id.postButton); // Button for submitting the post
        feedTextView = findViewById(R.id.feedTextView); // TextView for displaying feed updates or notifications

        // Listeners for buttons will be added later (if not already included in future updates)
    }
}
