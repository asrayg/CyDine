package com.example.androidexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUploadActivity extends AppCompatActivity {

    Button selectBtn;
    Button uploadBtn;
    ImageView mImageView;
    ProgressBar uploadProgressBar;
    Uri selectedUri;

    private static final String UPLOAD_URL = "http://10.0.2.2:8080/images";

    private ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        mImageView = findViewById(R.id.imageSelView);
        selectBtn = findViewById(R.id.selectBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);

        // Select image from gallery
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedUri = uri;
                        mImageView.setImageURI(uri);
                        Log.d("ImageUpload", "Image selected: " + selectedUri.toString());
                    } else {
                        Log.e("ImageUpload", "No image selected");
                    }
                });

        selectBtn.setOnClickListener(v -> mGetContent.launch("image/*"));
        uploadBtn.setOnClickListener(v -> uploadImage());
    }

    /**
     * Uploads an image to a remote server using a multipart Volley request.
     */
    private void uploadImage() {
        if (selectedUri == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            Log.e("ImageUpload", "No image URI found");
            return;
        }

        try {
            byte[] imageData = convertImageUriToBytes(selectedUri);
            if (imageData == null) {
                Log.e("ImageUpload", "Image data is null, upload failed");
                return;
            }

            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadProgressBar.setProgress(0);

            MultipartRequestWithProgress multipartRequest = new MultipartRequestWithProgress(
                    Request.Method.POST,
                    UPLOAD_URL,
                    imageData,
                    response -> {
                        Toast.makeText(getApplicationContext(), "Upload successful!", Toast.LENGTH_LONG).show();
                        Log.d("Upload", "Response: " + response);
                        uploadProgressBar.setVisibility(View.GONE);
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), "Upload failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Upload", "Error: " + error.getMessage());
                        uploadProgressBar.setVisibility(View.GONE);
                    },
                    progress -> uploadProgressBar.setProgress(progress)
            );

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);

        } catch (Exception e) {
            Log.e("ImageUpload", "Error during upload: ", e);
            Toast.makeText(getApplicationContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            uploadProgressBar.setVisibility(View.GONE);
        }
    }

    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            Log.d("ImageUpload", "Converting URI to byte array: " + imageUri.toString());
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            if (inputStream == null) {
                Log.e("ImageUpload", "InputStream is null for URI: " + imageUri.toString());
                throw new FileNotFoundException("InputStream could not be opened for URI: " + imageUri.toString());
            }

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            Log.d("ImageUpload", "Conversion successful");
            return byteBuffer.toByteArray();

        } catch (IOException e) {
            Log.e("ImageUpload", "IOException while reading file: " + imageUri.toString(), e);
            Toast.makeText(getApplicationContext(), "Error reading file: " + imageUri.toString(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    // Inner class to handle upload progress tracking
    private static class MultipartRequestWithProgress extends MultipartRequest {

        private final ProgressListener listener;
        private final byte[] fileData; // Add this line to define fileData

        public MultipartRequestWithProgress(int method, String url, byte[] fileData,
                                            Response.Listener<String> listener,
                                            Response.ErrorListener errorListener,
                                            ProgressListener progressListener) {
            super(method, url, fileData, listener, errorListener);
            this.fileData = fileData; // Initialize the fileData
            this.listener = progressListener;
        }


        public void writeBody(OutputStream output) throws IOException {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length;
            int uploaded = 0;
            int totalLength = fileData.length;

            for (int i = 0; i < totalLength; i += bufferSize) {
                length = Math.min(bufferSize, totalLength - i);
                output.write(fileData, i, length);
                uploaded += length;

                int progress = (int) ((uploaded / (float) totalLength) * 100);
                listener.onProgressUpdate(progress);
            }
        }

        public interface ProgressListener {
            void onProgressUpdate(int progress);
        }
    }
}
