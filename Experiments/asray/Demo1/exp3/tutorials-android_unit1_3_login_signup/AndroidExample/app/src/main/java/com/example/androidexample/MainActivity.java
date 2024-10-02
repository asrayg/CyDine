package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;
    private TextView usernameText;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);
        usernameText = findViewById(R.id.main_username_txt);
        logoutButton = findViewById(R.id.main_logout_btn);

        /* extract data passed into this activity from LoginActivity */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String username = extras.getString("USERNAME");
            usernameText.setText(username);
            welcomeUser(username);
        } else {
            usernameText.setVisibility(View.INVISIBLE); // if not logged in
            messageText.setText("Welcome, Guest");
            Toast.makeText(this, "You're viewing as a guest", Toast.LENGTH_SHORT).show();
        }

        /* logout functionality */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /* Custom greeting animation */
    private void welcomeUser(String username) {
        messageText.setText("Welcome, " + username);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1500);
        messageText.startAnimation(fadeIn);
    }
}
