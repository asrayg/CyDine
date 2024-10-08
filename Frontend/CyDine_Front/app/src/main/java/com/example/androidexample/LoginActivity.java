//package com.example.androidexample;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText email, password;
//
//    // Server URL for login
//    private static final String LOGIN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        email = findViewById(R.id.email);
//        password = findViewById(R.id.password);
//
//        Button loginButton = findViewById(R.id.login_button);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Validate input before sending data
//                if (validateInput()) {
//                    makeLoginRequest();
//                }
//            }
//        });
//
//        // Set up the TextView for sign-up redirect
//        TextView signUpRedirect = findViewById(R.id.sign_up_redirect);
//        signUpRedirect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Redirect to the SignUpActivity
//                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private boolean isValidEmail(String email) {
//        // Basic regex for email validation
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        return email.matches(emailPattern);
//    }
//
//    private boolean validateInput() {
//        String emailStr = email.getText().toString().trim();
//        String passwordStr = password.getText().toString().trim();
//
//        if (emailStr.isEmpty() || passwordStr.isEmpty()) {
//            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!isValidEmail(emailStr)) {
//            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        return true;
//    }
//
//    private void makeLoginRequest() {
//        // Create a GET request to fetch users
//        StringRequest loginRequest = new StringRequest(
//                Request.Method.GET,
//                LOGIN_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Log the raw server response
//                        Log.d("LoginResponse", response);
//                        parseResponse(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Enhanced error logging
//                        String errorMessage = "Unknown error occurred";
//                        if (error.networkResponse != null) {
//                            int statusCode = error.networkResponse.statusCode;
//                            String responseBody = new String(error.networkResponse.data);
//                            errorMessage = "Error " + statusCode + ": " + responseBody;
//                        }
//                        Log.e("LoginError", errorMessage);
//                        Toast.makeText(LoginActivity.this, "Error logging in: " + errorMessage, Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//
//        // Add the request to the Volley request queue
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
//    }
//
//    private void parseResponse(String response) {
//        try {
//            // Parse the response as a JSON array
//            JSONArray jsonArray = new JSONArray(response);
//
//            String emailInput = email.getText().toString().trim();
//            String passwordInput = password.getText().toString().trim();
//            boolean loginSuccess = false;
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject user = jsonArray.getJSONObject(i);
//
//                // Extract user details
//                String emailId = user.getString("emailId");
//                String password = user.getString("password");
//
//                // Check if the email and password match and the user is active
//                if (emailId.equals(emailInput) && password.equals(passwordInput)) {
//                        loginSuccess = true;
//                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
//
//                        // Redirect to MainActivity
//                        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
//                        startActivity(intent);
//                        finish(); // Finish LoginActivity so user can't go back
//                        break;
//                }
//            }
//
//            if (!loginSuccess) {
//                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(LoginActivity.this, "Error parsing server response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//}


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;

    // Server URL for login
    private static final String LOGIN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";

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
        // Create a GET request to fetch users
        StringRequest loginRequest = new StringRequest(
                Request.Method.GET,
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
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
    }

    private void parseResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();
            boolean loginSuccess = false;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject user = jsonArray.getJSONObject(i);

                String emailId = user.getString("emailId");
                String password = user.getString("password");

                if (emailId.equals(emailInput) && password.equals(passwordInput)) {
                    loginSuccess = true;
                    String userId = user.getString("id");
                    String userName = user.getString("name");  // Ensure name is extracted
                    String userEmail = user.getString("emailId");
                    Toast.makeText(LoginActivity.this, "Login successful! Your ID is: " + userId, Toast.LENGTH_LONG).show();

                    // Pass name, email, and ID to HomeScreenActivity
                    Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName); // Make sure this field is passed
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                    break;
                }
            }

            if (!loginSuccess) {
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Error parsing server response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

