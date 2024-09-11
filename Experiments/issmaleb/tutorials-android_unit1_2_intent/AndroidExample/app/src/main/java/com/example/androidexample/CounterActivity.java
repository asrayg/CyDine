package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private TextView highScoreTxt;
    private TextView feedbackMessage;
    private EditText targetScoreInput;
    private Button increaseBtn; // define increase button variable
    private Button decreaseBtn; // define decrease button variable
    private Button backBtn;     // define back button variable

    private int counter = 0;    // counter variable
    private int highScore = 0;
    private int targetScore = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        highScoreTxt = findViewById(R.id.highScore);
        feedbackMessage = findViewById(R.id.feedbackMessage);
        targetScoreInput = findViewById(R.id.targetScoreInput);
        increaseBtn = findViewById(R.id.counter_increase_btn);
        decreaseBtn = findViewById(R.id.counter_decrease_btn);
        backBtn = findViewById(R.id.counter_back_btn);

        numberTxt.setText(String.valueOf(counter));
        highScoreTxt.setText("High Score: " + highScore);

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(++counter));
                findViewById(R.id.counter_activity_layout).setBackgroundColor(Color.GREEN);

                // Update high score if needed
                if (counter > highScore) {
                    highScore = counter;
                    highScoreTxt.setText("High Score: " + highScore);
                }

                if(targetScore != -1 && targetScore == counter){
                    feedbackMessage.setText("Well done!");
                    Toast.makeText(CounterActivity.this, "Congratulations!", Toast.LENGTH_SHORT).show();
                    counter = 0;
                    findViewById(R.id.counter_activity_layout).setBackgroundColor(Color.WHITE); // Reset background color
                    targetScore = -1; // Clear target score
                    targetScoreInput.setText(""); // Clear the target score input field
                    targetScoreInput.requestFocus(); // Set focus to the target score input field
                    feedbackMessage.setText("Please enter a new target score."); // Prompt user to enter a new target score
                }

            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(--counter));
                findViewById(R.id.counter_activity_layout).setBackgroundColor(Color.YELLOW);
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });

        targetScoreInput.setOnEditorActionListener((v, actionId, event) -> {
            try {
                targetScore = Integer.parseInt(targetScoreInput.getText().toString());
                feedbackMessage.setText("Target Score Set: " + targetScore);
            } catch (NumberFormatException e) {
                feedbackMessage.setText("Invalid Score");
            }
            return true;
        });

    }
}