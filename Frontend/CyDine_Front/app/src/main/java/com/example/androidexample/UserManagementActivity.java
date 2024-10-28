package com.example.androidexample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapterActivity userAdapterActivity;
    private List<Map<String, Object>> userList;
    private List<Map<String, Object>> filteredUserList;
    private EditText searchBar;
    private Button searchButton;
    private MaterialButton deleteButton;

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
        populateDummyUsers();

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

    // Populate dummy users
    private void populateDummyUsers() {
        for (int i = 1; i <= 30; i++) {
            Map<String, Object> user = new HashMap<>();
            user.put("name", "User " + i);
            user.put("email", "user" + i + "@example.com");
            user.put("password", "password" + i);
            user.put("selected", false); // Track selection state
            userList.add(user);
        }
        filteredUserList.addAll(userList);
    }

    // Filter users based on search input
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

    // Check if any users are selected
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

    // Delete selected users
    private void deleteSelectedUsers() {
        Iterator<Map<String, Object>> iterator = filteredUserList.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> user = iterator.next();
            if ((Boolean) user.get("selected")) {
                iterator.remove();
            }
        }
        userAdapterActivity.notifyDataSetChanged();
        checkForSelectedUsers();
        Toast.makeText(this, "Selected users deleted.", Toast.LENGTH_SHORT).show();
    }
}
