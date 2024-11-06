package com.example.androidexample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final Context context;
    private final List<String> imageUrls;

    // Constructor
    public ImageAdapter(Context context) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
    }

    // Add a new image URL to the list
    public void addImage(String imageUrl) {
        Log.d("ImageAdapter", "Adding image: " + imageUrl);
        imageUrls.add(imageUrl);
        Log.d("ImageAdapter", "Total images: " + imageUrls.size());
        notifyItemInserted(imageUrls.size() - 1); // Notify RecyclerView to add the item
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_card, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.cardImageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    // ViewHolder class for image card
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.cardImageView);
        }
    }
}
