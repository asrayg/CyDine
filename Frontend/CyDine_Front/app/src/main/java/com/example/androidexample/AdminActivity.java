package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private Button userManagement;
    private Button diningHallData;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        userManagement = findViewById(R.id.user_management_button);
        diningHallData = findViewById(R.id.dining_hall_data_button);

        userManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, UserManagementActivity.class);
                startActivity(intent);
            }
        });

        diningHallData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, DiningHallDataActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onPendingReportsClick(View view) {
        // Handle the click event for the pending reports card
        Intent intent = new Intent(this, PendingReportsActivity.class);
        startActivity(intent);
    }
}
