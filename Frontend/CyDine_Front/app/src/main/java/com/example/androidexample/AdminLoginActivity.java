package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * AdminLoginActivity handles the login functionality for admin users.
 * Admin users must provide valid credentials to access the AdminActivity.
 */
public class AdminLoginActivity extends AppCompatActivity {

    // UI components
    private EditText emailEditText; // Input field for the admin's email
    private EditText passwordEditText; // Input field for the admin's password
    private Button loginButton; // Button to initiate the login process

    // Hardcoded admin credentials for demonstration purposes
    private static final String ADMIN_EMAIL = "admin@example.com"; // Admin email address
    private static final String ADMIN_PASSWORD = "admin123"; // Admin password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // Inflate the admin login layout

        // Initialize UI components
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        // Set up a click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the input from the email and password fields
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validate the credentials
                if (isValidCredentials(email, password)) {
                    // If valid credentials, navigate to the AdminActivity
                    Intent intent = new Intent(AdminLoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish(); // Close the login activity to prevent returning with the back button
                } else {
                    // If credentials are invalid, show a toast message
                    Toast.makeText(AdminLoginActivity.this, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Validates the admin's credentials.
     *
     * @param email    The email entered by the user
     * @param password The password entered by the user
     * @return true if the credentials match the hardcoded admin email and password; false otherwise
     */
    private boolean isValidCredentials(String email, String password) {
        // Compare the entered email and password with the hardcoded values
        return email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD);
    }
}
