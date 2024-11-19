package com.example.androidexample.mod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.R;

/**
 * Activity for moderator login.
 * Provides a simple login screen where moderators can enter their credentials.
 * On successful login, navigates to the {@link ModActivity}.
 */
public class ModLoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private static final String ADMIN_EMAIL = "a@a.a"; // Change this to your admin email
    private static final String ADMIN_PASSWORD = "a"; // Change this to your admin password

    /**
     * Initializes the activity, sets up the UI components, and handles the login process.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Null otherwise.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modlogin); // Ensure it references the login layout

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check credentials
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (isValidCredentials(email, password)) {
                    // If valid, navigate to AdminActivity
                    Intent intent = new Intent(ModLoginActivity.this, ModActivity.class);
                    startActivity(intent);
                    finish(); // Finish this activity
                } else {
                    // Show error message
                    Toast.makeText(ModLoginActivity.this, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Validates the provided email and password against hardcoded credentials.
     *
     * @param email    The entered email.
     * @param password The entered password.
     * @return True if the credentials are valid, false otherwise.
     */
    private boolean isValidCredentials(String email, String password) {
        // Simple validation logic
        return email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD);
    }
}
