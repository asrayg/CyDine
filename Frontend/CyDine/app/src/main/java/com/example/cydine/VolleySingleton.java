package com.example.cydine;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class to manage all network requests using Volley across the app.
 */
public class VolleySingleton {
    // Single instance of VolleySingleton
    private static VolleySingleton instance;

    // The request queue for managing network requests
    private RequestQueue requestQueue;

    // The image loader for managing image requests with caching
    private ImageLoader imageLoader;

    // Application context to avoid leaking activity contexts
    private static Context ctx;

    // Private constructor for Singleton pattern
    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        // ImageLoader with LruCache for caching images
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    // Method to get the single instance of VolleySingleton
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context.getApplicationContext());
        }
        return instance;
    }

    // Method to get the request queue, creating it if necessary
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // Creating the request queue with the application context
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    // Method to add a request to the request queue
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    // Method to get the image loader instance
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
