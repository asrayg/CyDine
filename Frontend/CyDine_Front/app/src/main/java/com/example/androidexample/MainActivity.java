package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MainActivity is the entry point of the app. It displays two buttons:
 * one for signing up and another for logging in. Clicking on either
 * button navigates the user to the corresponding activity.
 */
public class MainActivity extends AppCompatActivity {

    private Button signupButton, loginButton;

    /**
     * Called when the activity is first created.
     * Sets up the UI components and defines the click behavior for the buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized, this
     *                           bundle contains the data it most recently supplied.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserModAdminActivity.class);
                startActivity(intent);
            }
        });

    }
}