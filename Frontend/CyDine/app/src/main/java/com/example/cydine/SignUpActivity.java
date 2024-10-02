package com.example.cydine;

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

public class SignUpActivity extends AppCompatActivity {

    private EditText firstName, lastName, email, password, confirmPassword;

    // Mock server POST URL for sign-up
    private static final String SIGNUP_URL = "https://9aa89b97-07c9-45c0-8b97-da676a641a5d.mock.pstmn.io/info";

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
        if (!passwordStr.equals(confirmPasswordStr)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void makeSignUpRequest() {
        // Create a POST request for sign-up
        StringRequest signUpRequest = new StringRequest(
                Request.Method.POST,
                SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the mock server
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
                        // Handle error from the mock server
                        Log.e("SignUpError", error.toString());
                        Toast.makeText(SignUpActivity.this, "Error signing up: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Send form data as key-value pairs to the mock server
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName.getText().toString().trim());
                params.put("last_name", lastName.getText().toString().trim());
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(signUpRequest);
    }
}
