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

public class AdminActivity extends AppCompatActivity {

    private Button userManagement;
    private Button diningHallData;
    private TextView activeUsersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        userManagement = findViewById(R.id.user_management_button);
        diningHallData = findViewById(R.id.dining_hall_data_button);
        activeUsersCount = findViewById(R.id.active_users_count);

        userManagement.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, UserManagementActivity.class);
            startActivity(intent);
        });

        diningHallData.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, DiningHallDataActivity.class);
            startActivity(intent);
        });
        fetchActiveUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if the Intent has the flag to update active users
        if (getIntent().getBooleanExtra("updateActiveUserCount", false)) {
            fetchActiveUsers(); // Refresh active user count
            getIntent().removeExtra("updateActiveUserCount"); // Clear the flag
        }
    }

    private void fetchActiveUsers() {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int activeUserCount = 0;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if (response.getJSONObject(i).getBoolean("ifActive")) {
                                    activeUserCount++;
                                }
                            } catch (JSONException e) {
                                Log.e("AdminActivity", "JSON parsing error: " + e.getMessage());
                            }
                        }
                        activeUsersCount.setText("Active Users: " + activeUserCount);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AdminActivity", "Error fetching active users: " + error.getMessage());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void onPendingReportsClick(View view) {
        Intent intent = new Intent(this, PendingReportsActivity.class);
        startActivity(intent);
    }
}
