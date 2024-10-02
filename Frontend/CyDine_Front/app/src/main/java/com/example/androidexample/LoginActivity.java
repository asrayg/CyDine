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

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;

    // Mock server POST URL for login
    private static final String LOGIN_URL = "https://9aa89b97-07c9-45c0-8b97-da676a641a5d.mock.pstmn.io/login";

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
                        // Handle response from the mock server
                        Log.d("LoginResponse", response);

                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        // Redirect to MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error from the mock server
                        Log.e("LoginError", error.toString());
                        Toast.makeText(LoginActivity.this, "Error logging in: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Send form data as key-value pairs to the mock server
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                // Set Content-Type as form-urlencoded
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
    }
}
