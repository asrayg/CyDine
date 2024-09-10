package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;     // define message textview variable
    private Button counterButton;     // define counter button variable
    private EditText inputText;       // user input field for a personalized message
    private Button updateMsgButton;   // button to update the message
    private Button randomNumButton;   // button to generate a random number
    private Switch themeSwitch;       // theme switcher for dark mode/light mode

    private boolean isDarkMode = false;  // variable to track theme mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML
        inputText = findViewById(R.id.input_text);          // link to input field
        updateMsgButton = findViewById(R.id.update_msg_btn);// link to update message button
        randomNumButton = findViewById(R.id.random_num_btn);// link to random number button
        themeSwitch = findViewById(R.id.theme_switch);      // link to theme switch

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            messageText.setText("Intent Example");
        } else {
            String number = extras.getString("NUM");  // this will come from CounterActivity
            messageText.setText("The number was " + number);
        }

        /* click listener on counter button pressed */
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(counterButton);
                /* switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on update message button */
        updateMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = inputText.getText().toString();  // get text from user input
                if (!userMessage.isEmpty()) {
                    messageText.setText(userMessage);   // update the message text view
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* click listener on random number button */
        randomNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomNum = generateRandomNumber(1, 100);  // generate a random number
                messageText.setText("Random Number: " + randomNum);  // display random number
            }
        });

        /* theme switch listener */
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setDarkMode();  // enable dark mode
            } else {
                setLightMode(); // enable light mode
            }
        });
    }

    /* method to generate a random number */
    private int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /* method to animate button */
    private void animateButton(View button) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.1f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    /* method to enable dark mode */
    private void setDarkMode() {
        isDarkMode = true;
        findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.black));
        messageText.setTextColor(getResources().getColor(R.color.white));
        inputText.setTextColor(getResources().getColor(R.color.white));
        inputText.setHintTextColor(getResources().getColor(R.color.white));
    }

    /* method to enable light mode */
    private void setLightMode() {
        isDarkMode = false;
        findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.white));
        messageText.setTextColor(getResources().getColor(R.color.black));
        inputText.setTextColor(getResources().getColor(R.color.black));
        inputText.setHintTextColor(getResources().getColor(R.color.black));
    }
}
