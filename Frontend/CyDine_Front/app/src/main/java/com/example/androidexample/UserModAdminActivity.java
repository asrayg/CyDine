package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserModAdminActivity extends AppCompatActivity {

    private Button user, admin, mod;

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
                Intent intent = new Intent(UserModAdminActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserModAdminActivity.this, ModActivity.class);
                startActivity(intent);
            }
        });

    }
}
