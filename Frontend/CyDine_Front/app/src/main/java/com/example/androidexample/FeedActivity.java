package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView feedRecyclerView;
    private Button createPostButton;
    private ArrayList<Post> postList;
    private FeedAdapter feedAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedRecyclerView = findViewById(R.id.feedRecyclerView);
        createPostButton = findViewById(R.id.createPostButton);

        postList = new ArrayList<>();
        feedAdapter = new FeedAdapter(postList, this);

        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerView.setAdapter(feedAdapter);

        createPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String imagePath = data.getStringExtra("imagePath");
            String caption = data.getStringExtra("caption");
            postList.add(new Post(imagePath, caption));
            feedAdapter.notifyDataSetChanged();
        }
    }
}
