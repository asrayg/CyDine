package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView messageText;
    private Button backButton; // Define the Button variable
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initialize UI elements
        messageText = findViewById(R.id.second_msg_txt);
        imageView = findViewById(R.id.myImageView);
        imageView.setImageResource(R.drawable.sunset);
        messageText.setText("Welcome to the Second Activity!");

        backButton = findViewById(R.id.backButton); // Initialize the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close this activity and return to MainActivity
                finish();
            }
        });
    }
}
