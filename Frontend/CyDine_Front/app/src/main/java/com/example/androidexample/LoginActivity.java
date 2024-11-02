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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;

    private static final String LOGIN_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";
    private static final String UPDATE_USER_URL = LOGIN_URL; // Same URL base for updates

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
                if (validateInput()) {
                    makeLoginRequest();
                }
            }
        });

        TextView signUpRedirect = findViewById(R.id.sign_up_redirect);
        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
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
        StringRequest loginRequest = new StringRequest(
                Request.Method.GET,
                LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LoginResponse", response);
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                    String userName = user.getString("name");
                    String userEmail = user.getString("emailId");

                    // Update ifActive status to true
                    updateUserStatus(userId);

                    Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
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

    private void updateUserStatus(String userId) {
        // Step 1: Fetch current user data
        StringRequest fetchRequest = new StringRequest(
                Request.Method.GET,
                UPDATE_USER_URL + "/" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userData = new JSONObject(response);
                            // Step 2: Update only the ifActive field to true
                            userData.put("ifActive", true);

                            // Proceed to Step 3: Send the updated data back to the server
                            sendUpdateRequest(userId, userData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error parsing user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchUserError", "Error fetching user data: " + error.toString());
                        Toast.makeText(LoginActivity.this, "Error fetching user data", Toast.LENGTH_LONG).show();
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(fetchRequest);
    }

    private void sendUpdateRequest(String userId, JSONObject updatedData) {
        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                UPDATE_USER_URL + "/" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateResponse", response);
                        Toast.makeText(LoginActivity.this, "User status updated!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateError", "Error updating user status: " + error.toString());
                        if (error.networkResponse != null) {
                            String errorData = new String(error.networkResponse.data);
                            Log.e("UpdateErrorDetails", errorData);
                        } else {
                            Log.e("UpdateError", "Network error: " + error.getMessage());
                            Toast.makeText(LoginActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                try {
                    String jsonString = updatedData.toString();
                    Log.d("UpdateRequestBody", jsonString);
                    return jsonString.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("UpdateError", "Error encoding JSON body", e);
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(updateRequest);
    }
}
