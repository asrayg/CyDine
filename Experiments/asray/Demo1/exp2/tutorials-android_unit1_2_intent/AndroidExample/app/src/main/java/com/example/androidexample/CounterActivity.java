package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // counter display
    private TextView historyTxt; // operation history display
    private Button increaseBtn, decreaseBtn, multiplyBtn, divideBtn, resetBtn, customOpBtn, doubleBtn, backBtn;
    private EditText customInput;
    private int counter = 0; // initial counter value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        historyTxt = findViewById(R.id.history); // for operation history
        increaseBtn = findViewById(R.id.counter_increase_btn);
        decreaseBtn = findViewById(R.id.counter_decrease_btn);
        multiplyBtn = findViewById(R.id.counter_multiply_btn);
        divideBtn = findViewById(R.id.counter_divide_btn);
        doubleBtn = findViewById(R.id.counter_double_btn); // button to double the counter
        resetBtn = findViewById(R.id.counter_reset_btn);   // button to reset the counter
        customOpBtn = findViewById(R.id.counter_custom_btn); // button for custom multiplier/divider
        customInput = findViewById(R.id.custom_input);  // custom input field
        backBtn = findViewById(R.id.counter_back_btn);

        /* display initial counter value */
        numberTxt.setText(String.valueOf(counter));

        /* when increase button is pressed, increment counter */
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                updateCounter("Incremented by 1");
            }
        });

        /* when decrease button is pressed, decrement counter */
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                updateCounter("Decremented by 1");
            }
        });

        /* when multiply button is pressed, multiply counter by 2 */
        multiplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter *= 2;
                updateCounter("Multiplied by 2");
            }
        });

        /* when divide button is pressed, divide counter by 2 (if counter != 0) */
        divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter != 0) {
                    counter /= 2;
                    updateCounter("Divided by 2");
                }
            }
        });

        /* when reset button is pressed, set counter to 0 */
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                updateCounter("Reset to 0");
            }
        });

        /* when double button is pressed, double the counter value */
        doubleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter *= 2;
                updateCounter("Doubled");
            }
        });

        /* when custom operation button is pressed, use input value to multiply/divide */
        customOpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int inputValue = Integer.parseInt(customInput.getText().toString());
                    counter *= inputValue;
                    updateCounter("Multiplied by " + inputValue);
                } catch (NumberFormatException e) {
                    // Handle invalid input (not a number)
                    customInput.setError("Please enter a valid number");
                }
            }
        });

        /* when back button is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));
                startActivity(intent);
            }
        });
    }

    /* method to update the counter display and history log */
    private void updateCounter(String action) {
        numberTxt.setText(String.valueOf(counter));
        historyTxt.append(action + " -> New value: " + counter + "\n");
    }
}
