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

    private EditText firstName, lastName, email, password;
    private static final String UPDATE_USER_URL = "https://ea906bde-9d8c-444d-b333-aa2d4ea647fc.mock.pstmn.io/PUT";
    private static final String DELETE_USER_URL = "https://ea906bde-9d8c-444d-b333-aa2d4ea647fc.mock.pstmn.io/DEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.edit_first_name);
        lastName = findViewById(R.id.edit_last_name);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to update user info
                updateUserInformation();
            }
        });

        Button deleteButton = findViewById(R.id.button_delete_account);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to delete user account
                deleteUserAccount();
            }
        });
    }

    private void updateUserInformation() {
        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                UPDATE_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the mock server
                        Log.d("UpdateResponse", response);
                        Toast.makeText(ProfileActivity.this, "User information updated!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error from the mock server
                        Log.e("UpdateError", error.toString());
                        Toast.makeText(ProfileActivity.this, "Error updating user info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Send updated user information as key-value pairs
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName.getText().toString().trim());
                params.put("last_name", lastName.getText().toString().trim());
                params.put("email", email.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8"; // Ensure JSON content type
            }

            @Override
            public byte[] getBody() {
                try {
                    // Convert the parameters map to JSON string
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("first_name", firstName.getText().toString().trim());
                    jsonObject.put("last_name", lastName.getText().toString().trim());
                    jsonObject.put("email", email.getText().toString().trim());
                    jsonObject.put("password", password.getText().toString().trim());
                    return jsonObject.toString().getBytes("utf-8"); // Convert to bytes
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.e("UpdateError", "Error creating JSON body", e);
                    return null;
                }
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(updateRequest);
    }


    private void deleteUserAccount() {
        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                DELETE_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the mock server
                        Log.d("DeleteResponse", response);
                        Toast.makeText(ProfileActivity.this, "User account deleted!", Toast.LENGTH_SHORT).show();
                        // Redirect to LoginActivity after deletion
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close the ProfileActivity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error from the mock server
                        Log.e("DeleteError", error.toString());
                        Toast.makeText(ProfileActivity.this, "Error deleting account: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(deleteRequest);
    }
}
