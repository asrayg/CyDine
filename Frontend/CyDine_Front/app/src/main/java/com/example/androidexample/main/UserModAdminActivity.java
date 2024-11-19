package com.example.androidexample.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.R;
import com.example.androidexample.admin.AdminLoginActivity;
import com.example.androidexample.mod.ModLoginActivity;
import com.example.androidexample.user.LoginActivity;

/**
 * UserModAdminActivity allows users to choose their role (User, Admin, or Moderator)
 * and redirects them to the appropriate login screen.
 */
public class UserModAdminActivity extends AppCompatActivity {

    private Button user, admin, mod;

    /**
     * Initializes the activity and sets up button click listeners.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mod_admin);

        // Properly initialize buttons using findViewById
        user = findViewById(R.id.user);
        admin = findViewById(R.id.admin);
        mod = findViewById(R.id.mod);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserModAdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserModAdminActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserModAdminActivity.this, ModLoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
