package com.example.androidexample;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private ArrayList<Post> postList;
    private Context context;

    public FeedAdapter(ArrayList<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postImage.setImageURI(Uri.parse(post.getImageUri()));
        holder.captionText.setText(post.getCaption());
        holder.likeCountText.setText("Likes: " + post.getLikeCount());
        holder.commentsText.setText("Comments: " + post.getCommentsAsString());

        holder.likeButton.setOnClickListener(v -> {
            post.likePost();
            holder.likeCountText.setText("Likes: " + post.getLikeCount()); // Update like count
        });

        holder.commentButton.setOnClickListener(v -> {
            // For simplicity, we will add a sample comment
            post.addComment("Sample comment"); // This could be replaced with user input
            holder.commentsText.setText("Comments: " + post.getCommentsAsString()); // Update comments
        });

        holder.deleteButton.setOnClickListener(v -> {
            postList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, postList.size());
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView captionText, likeCountText, commentsText; // Add commentsText field
        Button likeButton, deleteButton, commentButton;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            captionText = itemView.findViewById(R.id.captionText);
            likeCountText = itemView.findViewById(R.id.likeCountText);
            commentsText = itemView.findViewById(R.id.commentsText); // Initialize commentsText
            likeButton = itemView.findViewById(R.id.likeButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            commentButton = itemView.findViewById(R.id.commentButton);
        }
    }
}
