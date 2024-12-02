package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeightActivity extends AppCompatActivity {

    private WeightGraphView weightGraphView;
    private LinearLayout weightHistoryContainer;
    private EditText weightInput;
    private Button addWeightButton, getProgressButton;

    private RequestQueue requestQueue;
    private static final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080/weight"; // Replace with your backend URL
    private String userId;
    private List<Float> weightData; // List to hold weight data for the graph

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Get userId from intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        weightGraphView = findViewById(R.id.weightGraphView); // Initialize the graph view
        weightHistoryContainer = findViewById(R.id.weightHistoryContainer);
        weightInput = findViewById(R.id.weightInput);
        addWeightButton = findViewById(R.id.addWeightButton);
        getProgressButton = findViewById(R.id.getProgressButton);

        weightData = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        // Fetch and display weight history on start
        fetchWeightHistory();

        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeightEntry();
            }
        });

        getProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightProgress();
            }
        });
    }

    private void fetchWeightHistory() {
        String url = BASE_URL + "/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            weightHistoryContainer.removeAllViews(); // Clear previous history
                            weightData.clear(); // Clear existing graph data

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject weightEntry = jsonArray.getJSONObject(i);
                                String date = weightEntry.getString("date");
                                double weight = weightEntry.getDouble("weight");
                                int id = weightEntry.getInt("id");

                                // Add weight data for the graph
                                weightData.add((float) weight);

                                // Create a card-like view for each entry
                                View entryView = getLayoutInflater().inflate(R.layout.weight_entry_card, weightHistoryContainer, false);
                                TextView dateTextView = entryView.findViewById(R.id.dateTextView);
                                TextView weightTextView = entryView.findViewById(R.id.weightTextView);
                                Button updateButton = entryView.findViewById(R.id.updateButton);
                                Button deleteButton = entryView.findViewById(R.id.deleteButton);

                                dateTextView.setText("Date: " + date);
                                weightTextView.setText("Weight: " + weight + " kg");

                                updateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateWeightEntry(id);
                                    }
                                });

                                deleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteWeightEntry(id);
                                    }
                                });

                                weightHistoryContainer.addView(entryView);
                            }

                            // Update the graph view
                            weightGraphView.setWeightData(weightData);

                        } catch (JSONException e) {
                            Log.e("Volley", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void addWeightEntry() {
        String weight = weightInput.getText().toString();

        if (weight.isEmpty()) {
            Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "/" + userId;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("weight", Double.parseDouble(weight));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(WeightActivity.this, "Weight entry added successfully!", Toast.LENGTH_SHORT).show();
                        fetchWeightHistory(); // Refresh weight history
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateWeightEntry(int id) {
        String weight = weightInput.getText().toString();

        if (weight.isEmpty()) {
            Toast.makeText(this, "Please enter weight for update", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "/" + userId + "/" + id;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("weight", Double.parseDouble(weight));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(WeightActivity.this, "Weight entry updated successfully!", Toast.LENGTH_SHORT).show();
                        fetchWeightHistory(); // Refresh weight history
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void deleteWeightEntry(int id) {
        String url = BASE_URL + "/" + userId + "/" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(WeightActivity.this, "Weight entry deleted successfully!", Toast.LENGTH_SHORT).show();
                        fetchWeightHistory(); // Refresh weight history
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void getWeightProgress() {
        String url = BASE_URL + "/" + userId + "/progress";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(WeightActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        requestQueue.add(stringRequest);
    }
}
