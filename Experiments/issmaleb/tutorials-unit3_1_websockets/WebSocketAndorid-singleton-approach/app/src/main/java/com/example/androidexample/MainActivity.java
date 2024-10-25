package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {

    private Button connectBtn, connectBtn2, backBtn, backBtn2;
    private EditText serverEtx, usernameEtx, serverEtx2, usernameEtx2;
    private Spinner statusSpinner1, statusSpinner2;
    private String selectedStatus1 = "Online"; // Default status
    private String selectedStatus2 = "Online"; // Default status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        connectBtn = findViewById(R.id.connectBtn);
        connectBtn2 = findViewById(R.id.connectBtn2);
        backBtn = findViewById(R.id.backBtn);
        backBtn2 = findViewById(R.id.backBtn2);
        serverEtx = findViewById(R.id.serverEdt);
        usernameEtx = findViewById(R.id.unameEdt);
        serverEtx2 = findViewById(R.id.serverEdt2);
        usernameEtx2 = findViewById(R.id.unameEdt2);
        statusSpinner1 = findViewById(R.id.statusSpinner1);
        statusSpinner2 = findViewById(R.id.statusSpinner2);

        // Set up status dropdowns
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner1.setAdapter(adapter);
        statusSpinner2.setAdapter(adapter);

        // Set item selected listeners for status spinners
        statusSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus1 = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        statusSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus2 = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Connect button listener for Chat Server 1
        connectBtn.setOnClickListener(view -> {
            String serverUrl = serverEtx.getText().toString();
            String username = usernameEtx.getText().toString();
            if (username.isEmpty()) {
                usernameEtx.setError("Username is required");
                return; // Prevent connection if username is empty
            }
            WebSocketManager1.getInstance().connectWebSocket(serverUrl + username);
            Intent intent = new Intent(this, ChatActivity1.class);
            intent.putExtra("status", selectedStatus1);
            startActivity(intent);
        });

        // Connect button listener for Chat Server 2
        connectBtn2.setOnClickListener(view -> {
            String serverUrl = serverEtx2.getText().toString();
            String username = usernameEtx2.getText().toString();
            if (username.isEmpty()) {
                usernameEtx2.setError("Username is required");
                return; // Prevent connection if username is empty
            }
            WebSocketManager2.getInstance().connectWebSocket(serverUrl + username);
            Intent intent = new Intent(this, ChatActivity2.class);
            intent.putExtra("status", selectedStatus2);
            startActivity(intent);
        });

        // Back button listeners
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity1.class);
            intent.putExtra("status", selectedStatus1);
            startActivity(intent);
        });

        backBtn2.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity2.class);
            intent.putExtra("status", selectedStatus2);
            startActivity(intent);
        });
    }
}
