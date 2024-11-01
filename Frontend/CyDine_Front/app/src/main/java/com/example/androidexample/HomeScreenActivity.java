package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String userId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String password = getIntent().getStringExtra("password");

        Intent intent = null;

        // Handle each menu item click
        if (item.getItemId() == R.id.nav_meals) {
            intent = new Intent(this, MealPlanActivity.class);
        } else if (item.getItemId() == R.id.nav_food) {
            intent = new Intent(this, FoodMenusActivity.class);
        } else if (item.getItemId() == R.id.nav_profile) {
            intent = new Intent(this, ProfileActivity.class);
        } else if (item.getItemId() == R.id.nav_water) {
            intent = new Intent(this, WaterActivity.class);
        } else if (item.getItemId() == R.id.nav_message) {
            intent = new Intent(this, MessageActivity.class);
        } else if (item.getItemId() == R.id.nav_exercise) {
            intent = new Intent(this, ExerciseActivity.class);
        } else if (item.getItemId() == R.id.nav_food_menu) {
            intent = new Intent(this, FoodMenuActivity.class);
        } else if (item.getItemId() == R.id.feed) {
            intent = new Intent(this, FeedActivity.class);
        }

        // Pass user details to the target activity if intent is created
        if (intent != null) {
            addUserDetailsToIntent(intent, userId, userName, userEmail, password);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addUserDetailsToIntent(Intent intent, String userId, String userName, String userEmail, String password) {
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("password", password);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
