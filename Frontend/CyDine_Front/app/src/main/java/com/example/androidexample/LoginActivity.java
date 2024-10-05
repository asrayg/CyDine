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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;

    // Server URL for login
    private static final String LOGIN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users"; // Keep the original URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input before sending data
                if (validateInput()) {
                    makeLoginRequest();
                }
            }
        });

        // Set up the TextView for sign-up redirect
        TextView signUpRedirect = findViewById(R.id.sign_up_redirect);
        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the SignUpActivity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
        // Basic regex for email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean validateInput() {
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if (emailStr.isEmpty() || passwordStr.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmail(emailStr)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void makeLoginRequest() {
        // Create a POST request for login
        StringRequest loginRequest = new StringRequest(
                Request.Method.POST,
                LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the raw server response
                        Log.d("LoginResponse", response);
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Enhanced error logging
                        String errorMessage = "Unknown error occurred";
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseBody = new String(error.networkResponse.data);
                            errorMessage = "Error " + statusCode + ": " + responseBody;
                        }
                        Log.e("LoginError", errorMessage);
                        Toast.makeText(LoginActivity.this, "Error logging in: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                // Create a JSON object with the user input fields
                String requestBody = "{\"emailId\":\"" + email.getText().toString().trim() + "\","
                        + "\"password\":\"" + password.getText().toString().trim() + "\"}";

                // Log the body being sent for debugging purposes
                Log.d("LoginRequestBody", requestBody);

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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
    }

    private void parseResponse(String response) {
        try {
            // Log the full response to see its structure
            Log.d("ResponseParsed", response);
            JSONObject jsonResponse = new JSONObject(response);

            // Check for a known structure in the response
            if (jsonResponse.has("success")) {
                boolean success = jsonResponse.getBoolean("success");

                if (success) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    // Redirect to MainActivity after successful login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            } else if (jsonResponse.has("error")) { // Handle error messages from the server
                String errorMessage = jsonResponse.getString("error");
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                // Handle unexpected response structure
                Toast.makeText(LoginActivity.this, "Unexpected response from server", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Error parsing server response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
