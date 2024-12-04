package com.example.androidexample;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MeditationActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private LinearLayout recordedSessionsContainer;
    private ProgressBar timerBar;
    private TextView timerTextView;
    private EditText timerGoal;
    private Button startBtn, resetBtn, stopBtn;
    private Handler handler = new Handler();
    private int totalTime; // in milliseconds
    private int elapsedTime = 0; // in milliseconds
    private boolean isTimerRunning = false;
    private ArrayList<String> recordedSessions = new ArrayList<>();
    private String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        timerBar = findViewById(R.id.timer_progress);
        timerTextView = findViewById(R.id.timer_text);
        timerGoal = findViewById(R.id.timer_goal_input);
        startBtn = findViewById(R.id.start_timer_button);
        resetBtn = findViewById(R.id.reset_timer_button);
        stopBtn = findViewById(R.id.stop_timer_button);
        recordedSessionsContainer = findViewById(R.id.recorded_sessions_container);
        userId = getIntent().getStringExtra("userId");

        startBtn.setOnClickListener(v->{
            if (!isTimerRunning) {
                startTimer();
            } else {
                Toast.makeText(MeditationActivity.this, "Timer is already running!", Toast.LENGTH_SHORT).show();
            }

        });

        resetBtn.setOnClickListener(v -> resetTimer());
        stopBtn.setOnClickListener(v -> stopTimer());

        fetchTodayMeditationRecords();
    }



    private void fetchTodayMeditationRecords() {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/users/" + userId + "/meditation/today";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    // Clear existing records before populating new data
                    recordedSessionsContainer.removeAllViews();

                    if (response.length() == 0) {
                        Toast.makeText(MeditationActivity.this, "No records found for today.", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                // Parse each JSON object in the response
                                JSONObject session = response.getJSONObject(i);

                                // Extract the details from the session object
                                String duration = session.getString("time") + " minutes";  // Duration is in the 'time' field
                                String startTime = session.getString("date");  // Start time is in the 'date' field

                                // Convert 'startTime' to a more readable format
                                String formattedStartTime = formatDate(startTime);

                                // Format the session record for display
                                String record = "Duration: " + duration + " | Start Time: " + formattedStartTime;

                                // Dynamically create and add a TextView for each session
                                TextView sessionTextView = new TextView(MeditationActivity.this);
                                sessionTextView.setText(record);
                                sessionTextView.setTextSize(16f);
                                sessionTextView.setPadding(8, 8, 8, 8);

                                recordedSessionsContainer.addView(sessionTextView);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MeditationActivity.this, "Error parsing session data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                error -> {
                    Toast.makeText(MeditationActivity.this, "Failed to load today's records: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }

    private String formatDate(String dateString) {
        try {
            // Define the input date format (the date format returned by the server)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            // Define the output date format for display
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy | HH:mm", Locale.getDefault());

            // Parse the date string into a Date object
            Date date = inputFormat.parse(dateString);

            // Return the formatted date as a string
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Date";
        }
    }



    private void startTimer() {
        String goal = timerGoal.getText().toString();
        if (goal.isEmpty()) {
            Toast.makeText(this, "Please set a meditation duration", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert goal (minutes) to milliseconds
        totalTime = Integer.parseInt(goal) * 60000;
        elapsedTime = 0;

        isTimerRunning = true;
        startBtn.setText("Pause Timer");

        startMeditationMusic();

        // Update the progress bar and time every second
        handler.postDelayed(timerRunnable, 1000);
    }
    private void startMeditationMusic() {
        if (mediaPlayer == null) {
            // Initialize the MediaPlayer to play the audio from the res/raw directory
            mediaPlayer = MediaPlayer.create(this, R.raw.meditation_music); // Make sure this file is in res/raw
            mediaPlayer.setLooping(true); // Loop the music
            mediaPlayer.start(); // Start the music
        }
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (elapsedTime < totalTime) {
                elapsedTime += 1000;
                int progress = (int) ((elapsedTime / (float) totalTime) * 100);
                timerBar.setProgress(progress);

                // Update the timer text (HH:MM)
                int minutes = (elapsedTime / 1000) / 60;
                int seconds = (elapsedTime / 1000) % 60;
                String timeText = String.format("%02d:%02d", minutes, seconds);
                timerTextView.setText(timeText);

                handler.postDelayed(this, 1000); // Keep updating every second
            } else {
                stopTimer();
                addSessionRecord(totalTime / 60000); // Record the session
            }
        }
    };
    private void addSessionRecord(int duration) {
        String currentDate = new SimpleDateFormat("MMM dd, yyyy | HH:mm", Locale.getDefault()).format(new Date());
        String sessionRecord = "Date: " + currentDate + " | Duration: " + duration + " minutes";

        recordedSessions.add(sessionRecord);

        // Dynamically add the session to the UI
        TextView sessionTextView = new TextView(this);
        sessionTextView.setText(sessionRecord);
        sessionTextView.setTextSize(16f);
        sessionTextView.setPadding(8, 8, 8, 8);

        recordedSessionsContainer.addView(sessionTextView);
        sendSessionToBackend(duration);
    }

    private void sendSessionToBackend(int duration) {
        String url = "http://coms-3090-020.class.las.iastate.edu:8080/meditation";

        // Create a JSON object with the required data
        Map<String, Object> params = new HashMap<>();
        params.put("time", duration);
        params.put("userId", userId);

        JSONObject jsonBody = new JSONObject(params);

        // Use StringRequest to handle non-JSONObject responses
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle a successful response
                    Toast.makeText(MeditationActivity.this, "Session saved successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("MeditationActivity", "Response: " + response);
                },
                error -> {
                    // Handle an error response
                    Toast.makeText(MeditationActivity.this, "Failed to save session: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MeditationActivity", "Error: ", error);
                }) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Add the request to the Volley RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            handler.removeCallbacks(timerRunnable); // Stop the timerRunnable from running
            startBtn.setText("Start Timer");
            Toast.makeText(MeditationActivity.this, "Timer stopped!", Toast.LENGTH_SHORT).show();

            stopMeditationMusic();

            // Reset the UI
            timerBar.setProgress(0); // Reset the progress bar
            timerTextView.setText("00:00"); // Reset the timer display
        }
        isTimerRunning = false;
        startBtn.setText("Start Timer");
        Toast.makeText(MeditationActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
    }

    private void stopMeditationMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();  // Stop the music
            mediaPlayer.release(); // Release the resources
            mediaPlayer = null;   // Set the media player to null
        }
    }

    private void resetTimer() {
        isTimerRunning = false;
        elapsedTime = 0;
        timerBar.setProgress(0);
        timerTextView.setText("00:00");
        startBtn.setText("Start Timer");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
