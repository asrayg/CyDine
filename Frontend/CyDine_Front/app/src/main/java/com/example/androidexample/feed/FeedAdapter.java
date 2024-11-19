package com.example.androidexample.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Ensure you import Glide
import com.example.androidexample.R;

import java.util.ArrayList;

/**
 * feed adapter for the feed activity
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private ArrayList<Post> postList; // List of posts to display
    private Context context; // Context for inflating views and accessing resources

    // Constructor to initialize the adapter with a list of posts and the context
    public FeedAdapter(ArrayList<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each post
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new FeedViewHolder(view); // Return a new ViewHolder instance
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        // Get the post at the current position
        Post post = postList.get(position);

        // Use Glide to load the image from the server into the ImageView
        Glide.with(context)
                .load(post.getImageUri()) // Load image from URI (URL or local path)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
                .into(holder.postImage); // Set the image in the ImageView

        // Set the caption text for the post
        holder.captionText.setText(post.getCaption());
    }

    @Override
    public int getItemCount() {
        return postList.size(); // Return the number of posts in the list
    }

    // ViewHolder class to hold references to the views for each item
    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage; // ImageView to display the post's image
        TextView captionText; // TextView to display the post's caption

        // Constructor to initialize the view references
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage); // Reference to the ImageView
            captionText = itemView.findViewById(R.id.captionText); // Reference to the TextView
        }
    }
}
