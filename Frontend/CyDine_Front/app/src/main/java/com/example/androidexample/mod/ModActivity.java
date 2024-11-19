package com.example.androidexample.mod;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.R;
import com.example.androidexample.TableAdapter;
import com.example.androidexample.TableItem;
import com.example.androidexample.message.MessageFeedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for moderators to manage reports and live message feeds.
 * Provides functionality to approve, dismiss, or ban users based on reports.
 */
public class ModActivity extends AppCompatActivity {

    private TextView messageDetails;
    private TextView reportCount;

    private RecyclerView tableRecyclerView;
    private RecyclerView liveMessageFeedRecyclerView;
    private TableAdapter tableAdapter;
    private MessageFeedAdapter messageFeedAdapter;

    /**
     * Initializes the activity, sets up the UI components, and populates data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Null otherwise.
     */
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

    /**
     * Provides sample data for the reports table.
     * Replace with backend data integration in production.
     *
     * @return A list of {@link TableItem} representing the sample reported items.
     */
    private List<TableItem> getSampleTableData() {
        List<TableItem> items = new ArrayList<>();
        items.add(new TableItem(true, "Asray", "Ish"));
        items.add(new TableItem(false, "User1", "User2"));
        return items;
    }

    /**
     * Provides sample data for the live message feed.
     * Replace with backend data integration in production.
     *
     * @return A list of strings representing sample messages.
     */
    private List<String> getSampleMessageFeedData() {
        List<String> messages = new ArrayList<>();
        messages.add("This is a sample message");
        messages.add("Another example of a message in the feed");
        return messages;
    }
}
