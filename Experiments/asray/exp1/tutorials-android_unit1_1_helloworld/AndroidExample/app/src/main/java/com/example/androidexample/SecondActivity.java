package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    private Button backButton;      // Define back button
    private RelativeLayout secondLayout;   // Layout reference to change background color
    private TextView quoteTextView;   // TextView for random quotes
    private Random random;           // Random object to generate random colors
    private Handler handler;         // Handler to change background color periodically
    private Runnable colorChanger;   // Runnable for background color changes
    private final int COLOR_CHANGE_INTERVAL = 3000; // Interval for color change (3 seconds)
    private String[] quotes = {      // Array of inspirational quotes
            "Believe in yourself!",
            "The sky is the limit!",
            "Dream big, work hard!",
            "Success is no accident.",
            "The journey is the reward.",
            "Never give up!"
    };
    private float x1, x2;            // Variables for gesture control

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initialize UI elements
        backButton = findViewById(R.id.back_button);
        secondLayout = findViewById(R.id.second_layout);
        quoteTextView = findViewById(R.id.quote_text_view);   // TextView for quotes
        random = new Random();
        handler = new Handler();

        // Set listener for back button to navigate back to MainActivity with sound and animation
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.animate().alpha(0f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        // Runnable to change background color and display a random quote with smooth transition
        colorChanger = new Runnable() {
            @Override
            public void run() {
                int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                secondLayout.animate().setDuration(1500).alpha(0.5f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        secondLayout.setBackgroundColor(color);
                        secondLayout.animate().alpha(1f).setDuration(1500);  // Smooth fade in/out transition
                    }
                });

                // Display a random quote
                String randomQuote = quotes[random.nextInt(quotes.length)];
                fadeInText(randomQuote);

                // Schedule the next color and quote change
                handler.postDelayed(this, COLOR_CHANGE_INTERVAL);
            }
        };

        // Start changing the background color and quotes periodically
        handler.post(colorChanger);
    }

    // Fade-in animation for quote
    private void fadeInText(String quote) {
        quoteTextView.setText(quote);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        quoteTextView.startAnimation(fadeIn);
    }

    // Handle swipe gestures to go back to MainActivity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (x1 > x2) {
                    Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the color changer when the activity is destroyed
        handler.removeCallbacks(colorChanger);

    }
}
