package com.example.androidexample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePostActivity extends AppCompatActivity {

    private ImageView selectedImageView;
    private EditText captionEditText;
    private Button selectImageButton, postButton;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        selectedImageView = findViewById(R.id.selectedImageView);
        captionEditText = findViewById(R.id.captionEditText);
        selectImageButton = findViewById(R.id.selectImageButton);
        postButton = findViewById(R.id.postButton);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        });

        postButton.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("imagePath", selectedImageUri.toString());
                resultIntent.putExtra("caption", captionEditText.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri);
        }
    }
}
