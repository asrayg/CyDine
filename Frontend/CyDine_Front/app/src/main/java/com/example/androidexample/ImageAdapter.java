package com.example.androidexample;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;  // Use EditText instead of TextView
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Context context;
    private final List<String> imageUrls = new ArrayList<>();
    private final List<Boolean> likedStatus = new ArrayList<>();
    private final List<List<String>> imageComments = new ArrayList<>(); // Store comments for each image

    public ImageAdapter(Context context) {
        this.context = context;
    }

    // Add new image with no comments initially
    public void addImage(String imageUrl) {
        imageUrls.add(imageUrl);
        likedStatus.add(false); // Default: not liked
        imageComments.add(new ArrayList<>()); // Initialize an empty list of comments
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context).load(imageUrl).into(holder.imageView);

        // Update the Like button text based on the liked status
        holder.likeButton.setText(likedStatus.get(position) ? "Unlike" : "Like");
        holder.likeButton.setOnClickListener(v -> {
            boolean isLiked = likedStatus.get(position);
            likedStatus.set(position, !isLiked);
            holder.likeButton.setText(!isLiked ? "Unlike" : "Like");
        });

        // Display comments for the current image
        List<String> comments = imageComments.get(position);
        if (comments.isEmpty()) {
            holder.commentsTextView.setVisibility(View.GONE); // Hide if no comments
        } else {
            holder.commentsTextView.setVisibility(View.VISIBLE);
            StringBuilder commentsText = new StringBuilder();
            for (String comment : comments) {
                commentsText.append(comment).append("\n");
            }
            holder.commentsTextView.setText(commentsText.toString()); // Display comments
        }

        // Add comment when the comment button is clicked
        holder.commentButton.setOnClickListener(v -> {
            // Show a dialog to allow the user to add a comment
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Add a Comment");

            // Create an EditText for input (not TextView)
            final EditText input = new EditText(context);  // Use EditText for comment input
            builder.setView(input);

            builder.setPositiveButton("Post", (dialog, which) -> {
                String comment = input.getText().toString().trim();
                if (!comment.isEmpty()) {
                    // Add the comment to the list of comments for the image
                    imageComments.get(position).add(comment);
                    notifyItemChanged(position); // Refresh this item's view to show the new comment
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final Button likeButton;
        public final Button commentButton;
        public final TextView commentsTextView; // TextView to show the comments

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardImageView);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            commentsTextView = itemView.findViewById(R.id.commentsTextView); // TextView for comments
        }
    }
}
