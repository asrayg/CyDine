package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class ImageReqActivity extends AppCompatActivity {

    private Button btnImageReq, btnGrayscale, btnSepia;
    private ImageView imageView;
    private Bitmap originalImage;

    public static final String URL_IMAGE = "http://sharding.org/outgoing/temp/testimg3.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_req);

        btnImageReq = findViewById(R.id.btnImageReq);
        btnGrayscale = findViewById(R.id.btnGrayscale);
        btnSepia = findViewById(R.id.btnSepia);
        imageView = findViewById(R.id.imgView);

        // Load and display the image on button click
        btnImageReq.setOnClickListener(v -> makeImageRequest());

        // Apply Grayscale filter
        btnGrayscale.setOnClickListener(v -> {
            if (originalImage != null) {
                Bitmap grayscaleImage = applyGrayscaleFilter(originalImage);
                imageView.setImageBitmap(grayscaleImage);
            } else {
                Toast.makeText(this, "Please load an image first", Toast.LENGTH_SHORT).show();
            }
        });

        // Apply Sepia filter
        btnSepia.setOnClickListener(v -> {
            if (originalImage != null) {
                Bitmap sepiaImage = applySepiaFilter(originalImage);
                imageView.setImageBitmap(sepiaImage);
            } else {
                Toast.makeText(this, "Please load an image first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Making image request
     */
    private void makeImageRequest() {
        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the original image in the ImageView
                        originalImage = response;
                        imageView.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(getApplicationContext(), "Image load failed!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    /**
     * Apply grayscale filter to a bitmap
     */
    private Bitmap applyGrayscaleFilter(Bitmap src) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(src, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * Apply sepia filter to a bitmap
     */
    private Bitmap applySepiaFilter(Bitmap src) {
        ColorMatrix sepiaMatrix = new ColorMatrix();
        sepiaMatrix.setScale(1, 0.95f, 0.82f, 1);
        Bitmap sepiaBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(sepiaBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(sepiaMatrix));
        canvas.drawBitmap(src, 0, 0, paint);
        return sepiaBitmap;
    }
}
