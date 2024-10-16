package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WaterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        // Initialize views
        EditText waterInput = findViewById(R.id.water_input);
        Button submitButton = findViewById(R.id.submit_button);

        // Set up button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waterAmount = waterInput.getText().toString();
                if (!waterAmount.isEmpty()) {
                    Toast.makeText(WaterActivity.this, "Water Amount: " + waterAmount + " ml", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WaterActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
