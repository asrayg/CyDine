package com.example.androidexample;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MeditationActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ProgressBar timerBar;
    private TextView timerTextView;
    private EditText timerGoal;
    private Button startBtn, resetBtn, stopBtn;
    private Handler handler = new Handler();
    private int totalTime; // in milliseconds
    private int elapsedTime = 0; // in milliseconds
    private boolean isTimerRunning = false;


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

        startBtn.setOnClickListener(v->{
            if (!isTimerRunning) {
                startTimer();
            } else {
                Toast.makeText(MeditationActivity.this, "Timer is already running!", Toast.LENGTH_SHORT).show();
            }

        });

        resetBtn.setOnClickListener(v -> resetTimer());
        stopBtn.setOnClickListener(v -> stopTimer());
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
            }
        }
    };

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
