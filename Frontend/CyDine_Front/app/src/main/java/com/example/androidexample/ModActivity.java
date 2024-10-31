package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModActivity extends AppCompatActivity {

    private TextView messageDetails;
    private TextView reportCount;

    private RecyclerView tableRecyclerView;
    private RecyclerView liveMessageFeedRecyclerView;
    private TableAdapter tableAdapter;
    private MessageFeedAdapter messageFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);

        messageDetails = findViewById(R.id.messageDetails);
        reportCount = findViewById(R.id.reportCount);

        // Setup RecyclerView for Table
        tableRecyclerView = findViewById(R.id.tableRecyclerView);
        tableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tableAdapter = new TableAdapter(getSampleTableData());
        tableRecyclerView.setAdapter(tableAdapter);

        // Setup RecyclerView for Live Message Feed
        liveMessageFeedRecyclerView = findViewById(R.id.liveMessageFeedRecyclerView);
        liveMessageFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageFeedAdapter = new MessageFeedAdapter(getSampleMessageFeedData());
        liveMessageFeedRecyclerView.setAdapter(messageFeedAdapter);

        // Button Listeners
        Button approveButton = findViewById(R.id.approveButton);
        Button dismissButton = findViewById(R.id.dismissButton);
        Button banUserButton = findViewById(R.id.banUserButton);

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ModActivity.this, "Message Approved", Toast.LENGTH_SHORT).show();
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ModActivity.this, "Message Dismissed", Toast.LENGTH_SHORT).show();
            }
        });

        banUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ModActivity.this, "User Banned", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Sample data for the table, replace with data from backend later
    private List<TableItem> getSampleTableData() {
        List<TableItem> items = new ArrayList<>();
        items.add(new TableItem(true, "Asray", "Ish"));
        items.add(new TableItem(false, "User1", "User2"));
        return items;
    }

    // Sample data for the message feed, replace with data from backend later
    private List<String> getSampleMessageFeedData() {
        List<String> messages = new ArrayList<>();
        messages.add("This is a sample message");
        messages.add("Another example of a message in the feed");
        return messages;
    }
}
