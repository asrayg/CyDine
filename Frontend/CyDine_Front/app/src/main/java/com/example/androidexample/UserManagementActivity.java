package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * UserManagementActivity provides functionality for managing user data.
 * Admins can view, search, and delete users through a RecyclerView-based interface.
 */
public class UserManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapterActivity userAdapterActivity;
    private List<Map<String, Object>> userList;
    private List<Map<String, Object>> filteredUserList;
    private EditText searchBar;
    private Button searchButton;
    private MaterialButton deleteButton;

    private static final String USER_URL = "http://coms-3090-020.class.las.iastate.edu:8080/users"; // Replace with your actual user URL

    /**
     * Initializes the activity, setting up views, fetching user data, and setting up listeners.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        // Initialize Views
        recyclerView = findViewById(R.id.recycler_view_users);
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        deleteButton = findViewById(R.id.delete_user_button);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize user lists
        userList = new ArrayList<>();
        filteredUserList = new ArrayList<>();

        // Fetch user data from the server
        fetchUsers();

        // Set up adapter
        userAdapterActivity = new UserAdapterActivity(filteredUserList, this::checkForSelectedUsers);
        recyclerView.setAdapter(userAdapterActivity);

        // Hide delete button initially
        deleteButton.setVisibility(View.GONE);

        // Set up search functionality
        searchButton.setOnClickListener(v -> filterUsers());

        // Set up delete button click listener
        deleteButton.setOnClickListener(v -> deleteSelectedUsers());
    }

    /**
     * Fetches the list of users from the backend server.
     */
    private void fetchUsers() {
        JsonArrayRequest userRequest = new JsonArrayRequest(
                Request.Method.GET,
                USER_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseUserData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserManagementActivity.this, "Error fetching users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add request to queue using Volley Singleton
        VolleySingleton.getInstance(this).addToRequestQueue(userRequest);
    }



    /**
     * Parses the JSON response to populate the user list.
     *
     * @param response The JSON array response containing user data.
     */
    private void parseUserData(JSONArray response) {
        try {
            userList.clear();
            for (int i = 0; i < response.length(); i++) {
                JSONObject userJson = response.getJSONObject(i);
                Map<String, Object> user = new HashMap<>();
                user.put("id", userJson.getString("id"));  // Assuming the JSON has an "id" field
                user.put("name", userJson.getString("name"));
                user.put("email", userJson.getString("emailId"));
                user.put("password", userJson.getString("password"));
                user.put("selected", false); // Track selection state
                userList.add(user);
            }
            filteredUserList.addAll(userList);
            userAdapterActivity.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing user data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Filters the user list based on the search input.
     */
    private void filterUsers() {
        String searchText = searchBar.getText().toString().trim();
        filteredUserList.clear();

        if (TextUtils.isEmpty(searchText)) {
            filteredUserList.addAll(userList);
        } else {
            for (Map<String, Object> user : userList) {
                String name = (String) user.get("name");
                if (name.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredUserList.add(user);
                }
            }
        }
        userAdapterActivity.notifyDataSetChanged();
        checkForSelectedUsers();
    }

    /**
     * Checks if any users are selected and updates the visibility of the delete button.
     */
    private void checkForSelectedUsers() {
        boolean hasSelectedUsers = false;
        for (Map<String, Object> user : filteredUserList) {
            if ((Boolean) user.get("selected")) {
                hasSelectedUsers = true;
                break;
            }
        }
        deleteButton.setVisibility(hasSelectedUsers ? View.VISIBLE : View.GONE);
    }

    /**
     * Deletes all selected users from the backend and updates the UI.
     */
    private void deleteSelectedUsers() {
        Iterator<Map<String, Object>> iterator = filteredUserList.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> user = iterator.next();
            if ((Boolean) user.get("selected")) {
                String userId = (String) user.get("id");
                deleteUserFromServer(userId);
                iterator.remove();
            }
        }
        userAdapterActivity.notifyDataSetChanged();
        checkForSelectedUsers();
        Toast.makeText(this, "Selected users deleted.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sends a request to the backend to delete a user by ID.
     *
     * @param userId The ID of the user to delete.
     */
    private void deleteUserFromServer(String userId) {
        String deleteUrl = USER_URL + "/" + userId;

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Notify AdminActivity to refresh the active users count
                        Intent intent = new Intent(UserManagementActivity.this, AdminActivity.class);
                        intent.putExtra("updateActiveUserCount", true);
                        startActivity(intent);
                        finish(); // Optionally finish this activity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserManagementActivity.this, "Error deleting user: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Add request to queue using Volley Singleton
        VolleySingleton.getInstance(this).addToRequestQueue(deleteRequest);
    }
}
