package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * AdminActivity serves as the main screen for the admin role, providing access to user management,
 * dining hall data, and displaying the count of currently active users.
 */
public class AdminActivity extends AppCompatActivity {

    // UI components
    private Button userManagement; // Button for navigating to user management
    private Button diningHallData; // Button for navigating to dining hall data
    private TextView activeUsersCount; // TextView to display the count of active users

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home); // Inflate the admin home layout

        // Initialize UI components
        userManagement = findViewById(R.id.user_management_button);
        diningHallData = findViewById(R.id.dining_hall_data_button);
        activeUsersCount = findViewById(R.id.active_users_count);

        // Set up click listener for the "User Management" button
        userManagement.setOnClickListener(view -> {
            // Navigate to UserManagementActivity when clicked
            Intent intent = new Intent(AdminActivity.this, UserManagementActivity.class);
            startActivity(intent);
        });

        // Set up click listener for the "Dining Hall Data" button
        diningHallData.setOnClickListener(view -> {
            // Navigate to DiningHallDataActivity when clicked
            Intent intent = new Intent(AdminActivity.this, DiningHallDataActivity.class);
            startActivity(intent);
        });

        // Fetch and display the count of active users from the backend
        fetchActiveUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if the Intent has the flag to update active users
        if (getIntent().getBooleanExtra("updateActiveUserCount", false)) {
            fetchActiveUsers(); // Refresh active user count
            getIntent().removeExtra("updateActiveUserCount"); // Clear the flag after use
        }
    }

    /**
     * Fetches the list of users from the backend API and counts the number of active users.
     */
    private void fetchActiveUsers() {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users"; // Backend endpoint for users

        // Create a GET request to fetch the user data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int activeUserCount = 0; // Counter for active users
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                // Check if the user is active
                                if (response.getJSONObject(i).getBoolean("ifActive")) {
                                    activeUserCount++;
                                }
                            } catch (JSONException e) {
                                Log.e("AdminActivity", "JSON parsing error: " + e.getMessage());
                            }
                        }
                        // Update the TextView to display the active user count
                        activeUsersCount.setText("Active Users: " + activeUserCount);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log error if the request fails
                Log.e("AdminActivity", "Error fetching active users: " + error.getMessage());
            }
        });

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Handles the click event for the "Pending Reports" button.
     * Navigates to PendingReportsActivity.
     *
     * @param view The view that was clicked
     */
    public void onPendingReportsClick(View view) {
        Intent intent = new Intent(this, PendingReportsActivity.class);
        startActivity(intent);
    }
}
