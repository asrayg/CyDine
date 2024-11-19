package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * healthy food recommendation maps
 */
public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    private static final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080/foodplaces";
    private WebView mapWebView;  // Changed from ImageView to WebView
    private LinearLayout restaurantListLayout;
    private RequestQueue requestQueue;

    /**
     * Called when the activity is created.
     * Initializes the views and fetches restaurant data and map image.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize views
        mapWebView = findViewById(R.id.mapImageView);  // Ensure ID matches the layout XML
        restaurantListLayout = findViewById(R.id.restaurantListLayout);
        requestQueue = Volley.newRequestQueue(this);

        // Configure WebView
        setupWebView();

        // Load map image and restaurant data
        loadMapImage();
        fetchRestaurantData();
    }

    /**
     * Sets up the WebView to display map images.
     */
    private void setupWebView() {
        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mapWebView.setWebViewClient(new WebViewClient());
    }

    /**
     * Loads a map image URL from the backend or uses a default URL.
     */
    private void loadMapImage() {
        // Set a default URL in case the backend does not return a valid image URL
        String defaultImageUrl = "https://ibb.c`o/f80PxcF";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, BASE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                JSONObject foodPlace = response.getJSONObject(0);
                                String imageUrl = foodPlace.optString("imageUrl", defaultImageUrl);

                                // Check if the image URL is empty or null, then set to default URL
                                if (imageUrl == null || imageUrl.isEmpty()) {
                                    Log.w(TAG, "No image URL provided by backend. Using default image URL.");
                                    imageUrl = defaultImageUrl;
                                } else {
                                    Log.i(TAG, "Image URL retrieved from backend: " + imageUrl);
                                }

                                // Load URL directly in WebView
                                mapWebView.loadUrl(imageUrl);
                            } else {
                                Log.e(TAG, "No food places found in response.");
                                // Load the default image if no data is returned
                                mapWebView.loadUrl(defaultImageUrl);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                            // Load the default image in case of JSON parsing errors
                            mapWebView.loadUrl(defaultImageUrl);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching food places: " + error.getMessage());
                        // Load the default image if there's an error in fetching the response
                        mapWebView.loadUrl(defaultImageUrl);
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Fetches restaurant data from the backend.
     */
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

    /**
     * Processes the restaurant data to remove duplicates and calculate the average rating.
     * @param restaurantArray The array of restaurant data retrieved from the backend.
     */
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

    /**
     * Populates the restaurant list layout with restaurant cards.
     * @param restaurantDataMap The map of restaurant data to be displayed.
     */
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

    /**
     * Displays the reviews dialog for a restaurant.
     * @param data The restaurant data to show in the dialog.
     */
    private void showReviewsDialog(RestaurantData data) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_reviews, null);
        TextView restaurantNameView = dialogView.findViewById(R.id.dialogRestaurantName);
        TextView averageRatingView = dialogView.findViewById(R.id.dialogAverageRating);
        LinearLayout reviewsLayout = dialogView.findViewById(R.id.reviewsLayout);

        restaurantNameView.setText(data.getName());
        averageRatingView.setText(String.format("Average Rating: %.1f", data.getAverageRating()));

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

    /**
     * Displays the rating dialog to allow a user to submit a review.
     * @param restaurantName The name of the restaurant being rated.
     */
    private void showRatingDialog(String restaurantName) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_ratings, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextView reviewInput = dialogView.findViewById(R.id.reviewInput);
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

    /**
     * Submits a review for a restaurant to the backend.
     * @param restaurantName The name of the restaurant being reviewed.
     * @param rating The rating given by the user.
     * @param reviewText The review text entered by the user.
     */
    private void submitReview(String restaurantName, float rating, String reviewText) {
        String url = BASE_URL;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", restaurantName);
            requestBody.put("rating", rating);
            requestBody.put("review", reviewText);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON request body: " + e.getMessage());
            return;
        }

        StringRequest postRequest = new StringRequest(
                Request.Method.POST, url,
                response -> {
                    Toast.makeText(MapActivity.this, "Review submitted!", Toast.LENGTH_SHORT).show();
                    fetchRestaurantData();  // Refresh the restaurant list after posting the review
                },
                error -> {
                    Log.e(TAG, "Error submitting review: " + error.getMessage());
                    Toast.makeText(MapActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
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

    /**
     * Represents a restaurant's data, including its name, ratings, and reviews.
     */
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
