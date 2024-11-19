package com.example.androidexample.admin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.R;

/**
 * Activity to display and manage pending reports.
 * This activity serves as a placeholder for showing a list of pending reports that require review.
 */
public class PendingReportsActivity extends AppCompatActivity {

    /**
     * Initializes the activity and sets up the layout for displaying pending reports.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Null otherwise.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_reports);
    }
}
