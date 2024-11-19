package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * SignUpActivity handles the user registration process by collecting user details,
 * validating input, and sending the data to a server for account creation.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText firstName, lastName, email, password, confirmPassword;

    // Server POST URL for sign-up
    private static final String SIGNUP_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";

    /**
     * Initializes the activity, setting up input fields, buttons, and event listeners.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        Button signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input before sending data
                if (validateInput()) {
                    makeSignUpRequest();
                }
            }
        });

        // Set up the TextView for login redirect
        TextView alreadyHaveAccount = findViewById(R.id.already_have_account);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the login page
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Validates if the given email is in a valid format.
     *
     * @param email The email string to validate.
     * @return True if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        // Basic regex for email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    /**
     * Validates user input fields for sign-up.
     *
     * @return True if all inputs are valid, false otherwise.
     */
    private boolean validateInput() {
        String firstNameStr = firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String confirmPasswordStr = confirmPassword.getText().toString().trim();

        if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmail(emailStr)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordStr.equals(confirmPasswordStr)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Sends a POST request to the server to create a new user account.
     */
    private void makeSignUpRequest() {
        // Create a POST request for sign-up
        StringRequest signUpRequest = new StringRequest(
                Request.Method.POST,
                SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the server
                        Log.d("SignUpResponse", response);

                        Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                        // Redirect to MainActivity
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error from the server
                        Log.e("SignUpError", error.toString());
                        Toast.makeText(SignUpActivity.this, "Error signing up: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                // Create a JSON object with the user input fields
                String fullName = firstName.getText().toString().trim() + " " + lastName.getText().toString().trim();
                String requestBody = "{\"name\":\"" + fullName + "\","
                        + "\"emailId\":\"" + email.getText().toString().trim() + "\","
                        + "\"password\":\"" + password.getText().toString().trim() + "\"}";

                // Log the body being sent for debugging purposes
                Log.d("SignUpRequestBody", requestBody);

                // Return the request body as a byte array (UTF-8 encoded)
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                // Set Content-Type as JSON
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(signUpRequest);
    }
}
