package com.example.androidexample;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.bumptech.glide.Glide; // Ensure you import Glide

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

        // Use Glide to load the image
        Glide.with(context)
                .load(Uri.parse(post.getImageUri()))
                .placeholder(R.drawable.ic_placeholder_image) // Optional placeholder
                .into(holder.postImage); // Load image using Glide

        holder.captionText.setText(post.getCaption());
        holder.likeCountText.setText("Likes: " + post.getLikeCount());
        holder.commentsText.setText("Comments: " + post.getCommentsAsString());

        holder.likeButton.setOnClickListener(v -> {
            post.likePost();
            holder.likeCountText.setText("Likes: " + post.getLikeCount());
        });

        holder.commentButton.setOnClickListener(v -> {
            post.addComment("Sample comment");
            holder.commentsText.setText("Comments: " + post.getCommentsAsString());
        });

        holder.commentButton.setOnClickListener(v -> {
            // Create an EditText for user input
            final EditText commentInput = new EditText(context);
            commentInput.setHint("Add a comment...");

            // Create the AlertDialog
            new AlertDialog.Builder(context)
                    .setTitle("Add Comment")
                    .setView(commentInput)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String comment = commentInput.getText().toString();
                        if (!comment.isEmpty()) {
                            post.addComment(comment); // Add the comment to the post
                            holder.commentsText.setText("Comments: " + post.getCommentsAsString());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView captionText, likeCountText, commentsText;
        Button likeButton, deleteButton, commentButton;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            captionText = itemView.findViewById(R.id.captionText);
            likeCountText = itemView.findViewById(R.id.likeCountText);
            commentsText = itemView.findViewById(R.id.commentsText);
            likeButton = itemView.findViewById(R.id.likeButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            commentButton = itemView.findViewById(R.id.commentButton);
        }
    }
}
