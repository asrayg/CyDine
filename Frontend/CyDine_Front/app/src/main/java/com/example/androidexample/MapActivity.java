package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    private static final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080/foodplaces";
    private ImageView mapImageView;
    private LinearLayout restaurantListLayout;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapImageView = findViewById(R.id.mapImageView);
        restaurantListLayout = findViewById(R.id.restaurantListLayout);
        requestQueue = Volley.newRequestQueue(this);

        loadMapImage();
        fetchRestaurantData();
    }

    private void loadMapImage() {
        String mapImageUrl = "image_url_from_backend"; // Replace with actual URL
        Glide.with(this).load(mapImageUrl).into(mapImageView);
    }

    private void fetchRestaurantData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, BASE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        processRestaurantData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching restaurants: " + error.getMessage());
                        Toast.makeText(MapActivity.this, "Failed to load restaurants", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void processRestaurantData(JSONArray restaurantArray) {
        Map<String, RestaurantData> restaurantDataMap = new HashMap<>();

        // Process data to remove duplicates and calculate average rating
        for (int i = 0; i < restaurantArray.length(); i++) {
            try {
                JSONObject restaurantObj = restaurantArray.getJSONObject(i);
                String name = restaurantObj.getString("name");
                double rating = restaurantObj.getDouble("rating");
                String review = restaurantObj.optString("review", "");

                // Check if the restaurant is already in the map
                if (restaurantDataMap.containsKey(name)) {
                    RestaurantData data = restaurantDataMap.get(name);
                    data.addRating(rating);
                    data.addReview(review);
                } else {
                    RestaurantData data = new RestaurantData(name);
                    data.addRating(rating);
                    data.addReview(review);
                    restaurantDataMap.put(name, data);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing restaurant data: " + e.getMessage());
            }
        }

        // Populate the UI with the processed restaurant data
        populateRestaurantList(restaurantDataMap);
    }

    private void populateRestaurantList(Map<String, RestaurantData> restaurantDataMap) {
        restaurantListLayout.removeAllViews();

        for (RestaurantData data : restaurantDataMap.values()) {
            View restaurantCard = LayoutInflater.from(this).inflate(R.layout.item_restaurant, restaurantListLayout, false);
            TextView restaurantNameView = restaurantCard.findViewById(R.id.restaurantName);
            TextView ratingView = restaurantCard.findViewById(R.id.restaurantRating);
            Button addReviewButton = restaurantCard.findViewById(R.id.addReviewButton);

            restaurantNameView.setText(data.getName());
            ratingView.setText(String.format("Average Rating: %.1f", data.getAverageRating()));

            // Show existing reviews when card is clicked
            restaurantCard.setOnClickListener(v -> showReviewsDialog(data));

            // Open rating dialog when "Add Review" button is clicked
            addReviewButton.setOnClickListener(v -> showRatingDialog(data.getName()));

            restaurantListLayout.addView(restaurantCard);
        }
    }


    private void showReviewsDialog(RestaurantData data) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_reviews, null);
        TextView restaurantNameView = dialogView.findViewById(R.id.dialogRestaurantName);
        TextView averageRatingView = dialogView.findViewById(R.id.dialogAverageRating);
        LinearLayout reviewsLayout = dialogView.findViewById(R.id.reviewsLayout);

        restaurantNameView.setText(data.getName());
        averageRatingView.setText(String.format("Average Rating: %.1f", data.getAverageRating()));

        // Populate the reviews
        for (String review : data.getReviews()) {
            TextView reviewTextView = new TextView(this);
            reviewTextView.setText(review);
            reviewTextView.setPadding(0, 10, 0, 10);
            reviewsLayout.addView(reviewTextView);
        }

        AlertDialog reviewsDialog = new AlertDialog.Builder(this)
                .setTitle("Reviews")
                .setView(dialogView)
                .setPositiveButton("Close", null)
                .create();
        reviewsDialog.show();
    }

    private void showRatingDialog(String restaurantName) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_ratings, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextView reviewInput = dialogView.findViewById(R.id.reviewInput); // Input for review text
        Button submitButton = dialogView.findViewById(R.id.submitRatingButton);

        AlertDialog ratingDialog = new AlertDialog.Builder(this)
                .setTitle("Rate " + restaurantName)
                .setView(dialogView)
                .create();

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String reviewText = reviewInput.getText().toString().trim();
            if (reviewText.isEmpty()) {
                Toast.makeText(this, "Review cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                submitReview(restaurantName, rating, reviewText);
                ratingDialog.dismiss();
            }
        });

        ratingDialog.show();
    }

    private void submitReview(String restaurantName, float rating, String reviewText) {
        String url = BASE_URL;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", restaurantName);   // Include the restaurant's name
            requestBody.put("rating", rating);         // Include the rating
            requestBody.put("review", reviewText);     // Include the review text
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON request body: " + e.getMessage());
            return;
        }

        StringRequest postRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MapActivity.this, "Review submitted!", Toast.LENGTH_SHORT).show();
                        fetchRestaurantData();  // Refresh the restaurant list after posting the review
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error submitting review: " + error.getMessage());
                        Toast.makeText(MapActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return requestBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(postRequest);
    }

    // Helper class to hold and calculate restaurant data
    private class RestaurantData {
        private final String name;
        private double totalRating;
        private int ratingCount;
        private final List<String> reviews = new ArrayList<>();

        RestaurantData(String name) {
            this.name = name;
        }

        void addRating(double rating) {
            totalRating += rating;
            ratingCount++;
        }

        void addReview(String review) {
            if (review != null && !review.isEmpty()) {
                reviews.add(review);
            }
        }

        double getAverageRating() {
            return ratingCount > 0 ? totalRating / ratingCount : 0;
        }

        String getName() {
            return name;
        }

        List<String> getReviews() {
            return reviews;
        }
    }
}