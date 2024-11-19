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

/**
 * Adapter class for displaying a list of images in a RecyclerView.
 * It binds a list of image URLs to a set of image views using Glide to load the images.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    /** Context of the activity or fragment that hosts the RecyclerView */
    private final Context context;

    /** List of image URLs to be displayed in the RecyclerView */
    private final List<String> imageUrls;

    /**
     * Constructor for the ImageAdapter.
     *
     * @param context The context in which the RecyclerView is used (e.g., activity or fragment).
     */
    public ImageAdapter(Context context) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
    }

    /**
     * Adds a new image URL to the list and notifies the adapter to update the RecyclerView.
     *
     * @param imageUrl The URL of the image to be added.
     */
    public void addImage(String imageUrl) {
        Log.d("ImageAdapter", "Adding image: " + imageUrl);
        imageUrls.add(imageUrl);
        Log.d("ImageAdapter", "Total images: " + imageUrls.size());
        // Notify RecyclerView that a new item has been added
        notifyItemInserted(imageUrls.size() - 1);
    }

    /**
     * Creates a new ViewHolder for an item in the RecyclerView.
     * This method is called when a new view is needed.
     *
     * @param parent The ViewGroup into which the new view will be added.
     * @param viewType The type of view created by this method.
     * @return A new ImageViewHolder.
     */
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the image_card layout to create a new view for an image item
        View view = LayoutInflater.from(context).inflate(R.layout.image_card, parent, false);
        return new ImageViewHolder(view);
    }

    /**
     * Binds the data (image URL) to the ViewHolder at the given position in the RecyclerView.
     * This method is called when a new item is displayed.
     *
     * @param holder The ViewHolder that should be updated with data.
     * @param position The position of the item within the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        // Use Glide to load the image from the URL and set it to the ImageView
        Glide.with(context)
                .load(imageUrl)
                .into(holder.cardImageView);
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The total number of image URLs in the list.
     */
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    /**
     * ViewHolder class for each item in the RecyclerView.
     * This holds the references to the views that are used to display an image.
     */
    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        /** ImageView that displays the image in each card */
        ImageView cardImageView;

        /**
         * Constructor for ImageViewHolder.
         *
         * @param itemView The view of the image card (inflated layout).
         */
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the ImageView for the image card
            cardImageView = itemView.findViewById(R.id.cardImageView);
        }
    }
}
