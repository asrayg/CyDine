package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText name, email, password;
    private EditText editHeight, editWeight, editAge;
    private EditText editFitnessGoals, editDietaryPreference;  // New EditText fields
    private static final String USER_DETAILS_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";
    private static final String UPDATE_USER_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";
    private static final String DELETE_USER_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize input fields
        name = findViewById(R.id.edit_name);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        editHeight = findViewById(R.id.edit_height);
        editWeight = findViewById(R.id.edit_weight);
        editAge = findViewById(R.id.edit_age);
        editFitnessGoals = findViewById(R.id.edit_fitness_goals);  // Initialize new field
        editDietaryPreference = findViewById(R.id.edit_dietary_preference);  // Initialize new field

        // Get the userId from intent
        String userId = getIntent().getStringExtra("userId");

        // Fetch the user details from server using userId
        fetchUserDetails(userId);

        // Save button action
        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation(userId); // Pass user ID to update function
            }
        });

        // Delete button action
        Button deleteButton = findViewById(R.id.button_delete_account);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPassword = password.getText().toString();
                if (userPassword.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please enter your password to delete the account.", Toast.LENGTH_SHORT).show();
                } else {
                    deleteUserAccount(userId);  // Pass userId
                }
            }
        });
    }

    // Function to fetch user details from the server
    private void fetchUserDetails(String userId) {
        String userUrl = USER_DETAILS_URL + "/" + userId;

        StringRequest userRequest = new StringRequest(
                Request.Method.GET,
                userUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // Populate fields from the JSON response
                            name.setText(jsonObject.getString("name"));
                            email.setText(jsonObject.getString("emailId"));
                            password.setText(jsonObject.getString("password"));
                            editHeight.setText(String.valueOf(jsonObject.optInt("height", 0)));
                            editWeight.setText(String.valueOf(jsonObject.optInt("weight", 0)));
                            editAge.setText(String.valueOf(jsonObject.optInt("age", 0)));
                            editFitnessGoals.setText(jsonObject.optString("fitness_goal", ""));  // Set fitness goals
                            editDietaryPreference.setText(jsonObject.optString("dietary_preference", ""));  // Set dietary preference

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Error parsing user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UserDetailsError", error.toString());
                        Toast.makeText(ProfileActivity.this, "Error fetching user details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(userRequest);
    }

    // Function to update user information
    private void updateUserInformation(String userId) {
        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                UPDATE_USER_URL + "/" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateResponse", response);
                        Toast.makeText(ProfileActivity.this, "User information updated!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateError", error.toString());
                        if (error.networkResponse != null) {
                            String errorData = new String(error.networkResponse.data);
                            Log.e("UpdateErrorDetails", errorData);
                            Toast.makeText(ProfileActivity.this, "Error updating user info: " + errorData, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", userId);
                    jsonObject.put("name", name.getText().toString().trim());
                    jsonObject.put("emailId", email.getText().toString().trim());
                    jsonObject.put("password", password.getText().toString().trim());
                    jsonObject.put("ifActive", true);
                    jsonObject.put("height", Integer.parseInt(editHeight.getText().toString().trim()));
                    jsonObject.put("weight", Integer.parseInt(editWeight.getText().toString().trim()));
                    jsonObject.put("age", Integer.parseInt(editAge.getText().toString().trim()));
                    jsonObject.put("fitness_goal", editFitnessGoals.getText().toString().trim()); // Add fitness goals to JSON
                    jsonObject.put("dietary_preference", editDietaryPreference.getText().toString().trim()); // Add dietary preference to JSON

                    return jsonObject.toString().getBytes("utf-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.e("UpdateError", "Error creating JSON body", e);
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

        Volley.newRequestQueue(this).add(updateRequest);
    }

    private void deleteUserAccount(String userId) {
        String deleteUrl = DELETE_USER_URL + "/" + userId;

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DeleteResponse", response);
                        Toast.makeText(getApplicationContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close this activity
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
                        Log.e("DeleteError", errorMessage);
                        Toast.makeText(getApplicationContext(), "Error deleting user: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        Volley.newRequestQueue(this).add(deleteRequest);
    }
}
