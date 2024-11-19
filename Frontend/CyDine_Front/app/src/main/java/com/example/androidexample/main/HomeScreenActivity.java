package com.example.androidexample.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidexample.R;
import com.example.androidexample.dhmeals.FoodMenuActivity;
import com.example.androidexample.exercise.ExerciseActivity;
import com.example.androidexample.feed.FeedActivity;
import com.example.androidexample.map.MapActivity;
import com.example.androidexample.message.MessageActivity;
import com.example.androidexample.outsideFood.FoodMenusActivity;
import com.example.androidexample.outsideFood.MealPlanActivity;
import com.example.androidexample.user.ProfileActivity;
import com.example.androidexample.water.WaterActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** DrawerLayout to manage the sliding navigation drawer */
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        /** Set up the toolbar and make it the app's action bar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Initialize DrawerLayout and NavigationView */
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        /** Set up ActionBarDrawerToggle to handle opening and closing of the navigation drawer */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); // Synchronize the state of the drawer toggle
    }

    /**
     * Handles navigation item selection and starts the corresponding activity.
     *
     * @param item The selected menu item
     * @return boolean indicating whether the item selection is handled
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Retrieve user details from the intent that started this activity
        String userId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String password = getIntent().getStringExtra("password");

        /** Variable to hold the intent for the selected activity */
        Intent intent = null;

        /** Handle navigation item selection */
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
        } else if (item.getItemId() == R.id.nav_map) {
            intent = new Intent(this, MapActivity.class);
        }

        /** If an intent was created for a menu item, add user details and start the activity */
        if (intent != null) {
            addUserDetailsToIntent(intent, userId, userName, userEmail, password);
            startActivity(intent);
        }

        /** Close the drawer after an item is selected */
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Adds the user details to the intent to pass them to the target activity.
     *
     * @param intent     The target activity intent
     * @param userId     The user's ID
     * @param userName   The user's name
     * @param userEmail  The user's email
     * @param password   The user's password
     */
    private void addUserDetailsToIntent(Intent intent, String userId, String userName, String userEmail, String password) {
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("password", password);
    }

    /**
     * Handles the back press event. If the drawer is open, it closes it; otherwise,
     * it performs the default back press action.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
