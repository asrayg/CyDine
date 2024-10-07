//package com.example.androidexample;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//
//public class ProfileActivity extends AppCompatActivity {
//
//    private EditText name, email, password;
//    private static final String UPDATE_USER_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";
//    private static final String DELETE_USER_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        // Initialize input fields
//        name = findViewById(R.id.edit_name);
//        email = findViewById(R.id.edit_email);
//        password = findViewById(R.id.edit_password);
//
//        // Get the user data from intent
//        String userId = getIntent().getStringExtra("userId");
//        String userName = getIntent().getStringExtra("userName");
//        String userEmail = getIntent().getStringExtra("userEmail");
//
//        // Pre-fill the user's name and email
//        name.setText(userName);  // Ensure name is set correctly
//        email.setText(userEmail);
//
//        // Save button action
//        Button saveButton = findViewById(R.id.button_save);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateUserInformation(userId); // Pass user ID to update function
//            }
//        });
//
//        // Delete button action
//        Button deleteButton = findViewById(R.id.button_delete_account);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteUserAccount(userId); // Pass user ID to delete function
//            }
//        });
//    }
//
//    // Function to update user information
//    private void updateUserInformation(String userId) {
//        StringRequest updateRequest = new StringRequest(
//                Request.Method.PUT,
//                UPDATE_USER_URL + "/" + userId, // Append user ID to URL
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("UpdateResponse", response);
//                        Toast.makeText(ProfileActivity.this, "User information updated!", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("UpdateError", error.toString());
//                        Toast.makeText(ProfileActivity.this, "Error updating user info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public byte[] getBody() {
//                try {
//                    // Create JSON object for the request body
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("name", name.getText().toString().trim()); // Pass the name
//                    jsonObject.put("email", email.getText().toString().trim());
//                    jsonObject.put("password", password.getText().toString().trim());
//                    return jsonObject.toString().getBytes("utf-8"); // Return the JSON body as byte array
//                } catch (JSONException | UnsupportedEncodingException e) {
//                    Log.e("UpdateError", "Error creating JSON body", e);
//                    return null;
//                }
//            }
//        };
//
//        // Add request to queue
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(updateRequest);
//    }
//
//    // Function to delete user account
//    private void deleteUserAccount(String userId) {
//        StringRequest deleteRequest = new StringRequest(
//                Request.Method.DELETE,
//                DELETE_USER_URL + "/" + userId, // Append user ID to URL
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("DeleteResponse", response);
//                        Toast.makeText(ProfileActivity.this, "User account deleted!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish(); // Finish current activity
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("DeleteError", error.toString());
//                        Toast.makeText(ProfileActivity.this, "Error deleting account: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        // Add request to queue
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(deleteRequest);
//    }
//}


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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText name, email, password;
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

        // Get the user data from intent
        String userId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");

        // Pre-fill the user's name and email
        name.setText(userName);
        email.setText(userEmail);

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
                deleteUserAccount(userId); // Pass user ID to delete function
            }
        });
    }

    // Function to update user information
    private void updateUserInformation(String userId) {
        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                UPDATE_USER_URL + "/" + userId, // Append user ID to URL
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
                            Log.e("UpdateErrorDetails", errorData); // Log server error response
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
                    // Create JSON object for the request body, including userId
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", userId);  // Pass the userId explicitly
                    jsonObject.put("name", name.getText().toString().trim()); // Pass the name
                    jsonObject.put("email", email.getText().toString().trim());
                    jsonObject.put("password", password.getText().toString().trim());

                    String jsonString = jsonObject.toString();
                    Log.d("UpdateRequestBody", jsonString); // Log the request body for debugging
                    return jsonString.getBytes("utf-8"); // Return the JSON body as byte array
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.e("UpdateError", "Error creating JSON body", e);
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");  // Set Content-Type as JSON
                return headers;
            }
        };

        // Add request to queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(updateRequest);
    }

    // Function to delete user account
    private void deleteUserAccount(String userId) {
        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                DELETE_USER_URL + "/" + userId, // Append user ID to URL
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DeleteResponse", response);
                        Toast.makeText(ProfileActivity.this, "User account deleted!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Finish current activity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DeleteError", error.toString());
                        if (error.networkResponse != null) {
                            String errorData = new String(error.networkResponse.data);
                            Log.e("DeleteErrorDetails", errorData); // Log server error response
                            Toast.makeText(ProfileActivity.this, "Error deleting account: " + errorData, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Add request to queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(deleteRequest);
    }
}
