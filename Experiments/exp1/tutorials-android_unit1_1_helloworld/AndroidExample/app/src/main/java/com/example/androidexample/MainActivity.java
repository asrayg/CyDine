package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import java.util.Random;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView clockText;     // define clock text view for real-time clock
    private Button changeTextButton, themeSwitchButton, confettiButton, secondScreenButton;  // buttons
    private Random random;
    private boolean isDarkTheme = false;  // Track the current theme
    private Handler clockHandler;  // Handler for clock updates

    // Greetings array for dynamic welcome message
    private String[] greetings = {
            "Hello, world!", "Welcome back!", "Good to see you!", "Let's start coding!",
            "Ready for an adventure?", "The world of Android awaits!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        messageText = findViewById(R.id.main_msg_txt);
        clockText = findViewById(R.id.clock_txt);
        changeTextButton = findViewById(R.id.change_text_button);
        themeSwitchButton = findViewById(R.id.theme_switch_button);
        confettiButton = findViewById(R.id.confetti_button);
        secondScreenButton = findViewById(R.id.second_screen_button);
        random = new Random();
        clockHandler = new Handler();


        // Set initial text with a dynamic message
        setRandomGreeting();

        // Button click to change text and color with animation
        changeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMessage = "Enjoy Your Coding Journey!";
                messageText.setText(newMessage);
                changeTextColorRandomly();
                Toast.makeText(MainActivity.this, "Cool you clicked that button!", Toast.LENGTH_SHORT).show();
            }
        });

        // Theme switcher button to toggle light and dark themes
        themeSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme();
            }
        });

        // Confetti button to show celebration animation
        confettiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfetti();
            }
        });

        // Navigate to the second screen
        secondScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        // Start the real-time clock
        startClock();
    }

    // Set a random greeting message from the greetings array
    private void setRandomGreeting() {
        String randomGreeting = greetings[random.nextInt(greetings.length)];
        messageText.setText(randomGreeting);
    }

    // Change the text color randomly
    private void changeTextColorRandomly() {
        int color = android.graphics.Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        messageText.setTextColor(color);
    }



    // Toggle between light and dark themes
    private void toggleTheme() {
        if (isDarkTheme) {
            setTheme(android.R.style.Theme_Light);
        } else {
            setTheme(android.R.style.Theme_Black);
        }
        isDarkTheme = !isDarkTheme;
        recreate();  // Recreate the activity to apply the theme change
    }

    // Display a confetti animation
    private void showConfetti() {
        Toast.makeText(this, "🎉 Confetti! 🎉", Toast.LENGTH_SHORT).show();
    }

    // Start the real-time clock that updates every second
    private void startClock() {
        clockHandler.post(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);
                String currentTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                clockText.setText(currentTime);

                // Update clock every second
                clockHandler.postDelayed(this, 1000);
            }
        });
    }


}
