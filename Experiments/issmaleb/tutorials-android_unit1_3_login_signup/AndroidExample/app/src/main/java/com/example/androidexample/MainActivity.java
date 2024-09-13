package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView usernameText;  // define username textview variable
    private Button loginButton;     // define login button variable
    private Button signupButton;    // define signup button variable
    private Button logoutButton;
    private Button settingsButton;

    private static final String PREFS_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        usernameText = findViewById(R.id.main_username_txt);// link to username textview in the Main activity XML
        loginButton = findViewById(R.id.main_login_btn);    // link to login button in the Main activity XML
        signupButton = findViewById(R.id.main_signup_btn);  // link to signup button in the Main activity XML
        logoutButton = findViewById(R.id.logout_btn);
        settingsButton = findViewById(R.id.settings_btn);


        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            messageText.setText("Home Page");
            usernameText.setVisibility(View.INVISIBLE);             // set username text invisible initially
        } else {
            messageText.setText("Welcome");
            usernameText.setText(extras.getString("USERNAME")); // this will come from LoginActivity
            loginButton.setVisibility(View.INVISIBLE);              // set login button invisible
            signupButton.setVisibility(View.INVISIBLE);             // set signup button invisible
            logoutButton.setVisibility(View.VISIBLE);               // show logout button
            settingsButton.setVisibility(View.VISIBLE);
        }

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on logout button pressed */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the stored username and password from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();  // Clear saved user data
                editor.apply();

                // Redirect to the LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear backstack
                startActivity(intent);
                finish();  // End the current MainActivity
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the data through the intent to the SettingsActivity
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                Bundle extras = getIntent().getExtras();
                intent.putExtra("USERNAME", extras.getString("USERNAME"));
                intent.putExtra("PASSWORD", extras.getString("PASSWORD"));  // Retrieve actual password securely
                startActivity(intent);
            }
        });
    }
}
