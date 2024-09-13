package com.example.androidexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;  // Import the correct LinearLayoutCompat
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private Switch settingsSwitch;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private LinearLayoutCompat usernameLayout;  // Use LinearLayoutCompat here
    private LinearLayoutCompat passwordLayout;  // Use LinearLayoutCompat here
    private static final String PREFS_NAME = "MyPrefs";
    private static final String SWITCH_STATE = "switch_state";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsSwitch = findViewById(R.id.settings_switch);
        usernameTextView = findViewById(R.id.settings_username_txt);
        passwordTextView = findViewById(R.id.settings_password_txt);
        usernameLayout = findViewById(R.id.username_layout);  // Link to LinearLayoutCompat
        passwordLayout = findViewById(R.id.password_layout);  // Link to LinearLayoutCompat

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSwitchChecked = sharedPreferences.getBoolean(SWITCH_STATE, false);

        // Retrieve the username and password from the intent extras
        Bundle extras = getIntent().getExtras();
        String username = extras != null ? extras.getString("USERNAME", sharedPreferences.getString("USERNAME", "")) : sharedPreferences.getString("USERNAME", "");
        String password = extras != null ? extras.getString("PASSWORD", sharedPreferences.getString("PASSWORD", "")) : sharedPreferences.getString("PASSWORD", "");

        usernameTextView.setText(username);
        passwordTextView.setText(password);

        // Show or hide credentials based on switch state
        if (isSwitchChecked) {
            showCredentials();
        } else {
            hideCredentials();
        }

        // Set an OnCheckedChangeListener to handle the switch toggle
        settingsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SWITCH_STATE, isChecked);
                editor.apply();

                if (isChecked) {
                    showCredentials();
                } else {
                    hideCredentials();
                }
            }
        });
    }

    // Method to show the username and password layouts
    private void showCredentials() {
        usernameLayout.setVisibility(View.VISIBLE);
        passwordLayout.setVisibility(View.VISIBLE);
    }

    // Method to hide the username and password layouts
    private void hideCredentials() {
        usernameLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.GONE);
    }
}
