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

public class SleepActivity extends AppCompatActivity {

    private LinearLayout sleepHistoryContainer;
    private EditText sleepHoursInput;
    private Button addSleepButton, getAssessmentButton;

    private SleepGraphView sleepGraphView;
    private List<Float> sleepData; // List to hold sleep data

    private RequestQueue requestQueue;
    private static final String BASE_URL = "http://coms-3090-020.class.las.iastate.edu:8080/sleep"; // Replace with your backend URL
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        // Get userId from intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        sleepGraphView = findViewById(R.id.sleepGraphView); // Initialize the graph view
        sleepData = new ArrayList<>(); // Initialize the list for graph data


        // Initialize UI components
        sleepHistoryContainer = findViewById(R.id.sleepHistoryContainer);
        sleepHoursInput = findViewById(R.id.sleepHoursInput);
        addSleepButton = findViewById(R.id.addSleepButton);
        getAssessmentButton = findViewById(R.id.getAssessmentButton);

        requestQueue = Volley.newRequestQueue(this);

        // Fetch and display sleep history on start
        fetchSleepHistory();

        addSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSleepEntry();
            }
        });

        getAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSleepAssessment();
            }
        });
    }

    private void fetchSleepHistory() {
        String url = BASE_URL + "/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            sleepHistoryContainer.removeAllViews(); // Clear previous history
                            sleepData.clear(); // Clear existing graph data

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject sleepEntry = jsonArray.getJSONObject(i);
                                String date = sleepEntry.getString("date");
                                double hoursSlept = sleepEntry.getDouble("hoursSlept");
                                int id = sleepEntry.getInt("id");

                                // Add sleep data for the graph
                                sleepData.add((float) hoursSlept);

                                // Create a card-like view for each entry
                                View entryView = getLayoutInflater().inflate(R.layout.sleep_entry_card, sleepHistoryContainer, false);
                                TextView dateTextView = entryView.findViewById(R.id.dateTextView);
                                TextView hoursTextView = entryView.findViewById(R.id.hoursTextView);
                                Button updateButton = entryView.findViewById(R.id.updateButton);
                                Button deleteButton = entryView.findViewById(R.id.deleteButton);

                                dateTextView.setText("Date: " + date);
                                hoursTextView.setText("Hours Slept: " + hoursSlept);

                                updateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateSleepEntry(id);
                                    }
                                });

                                deleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteSleepEntry(id);
                                    }
                                });

                                sleepHistoryContainer.addView(entryView);
                            }

                            // Update the graph view
                            sleepGraphView.setSleepData(sleepData);

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

    private void addSleepEntry() {
        String url = BASE_URL + "/" + userId + "/latest";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject latestEntry = new JSONObject(response);
                            String latestDate = latestEntry.getString("date");

                            // Check if the latest entry is for today
                            if (latestDate.equals(java.time.LocalDate.now().toString())) {
                                Toast.makeText(SleepActivity.this, "You have already added a sleep entry for today!", Toast.LENGTH_SHORT).show();
                            } else {
                                addNewSleepEntry(); // Call the method to add a new entry
                            }
                        } catch (JSONException e) {
                            addNewSleepEntry(); // No entry exists for today
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        addNewSleepEntry(); // Assume no entry exists on error
                    }
                });

        requestQueue.add(stringRequest);
    }

    // Separate method to actually add a new sleep entry
    private void addNewSleepEntry() {
        String hoursSlept = sleepHoursInput.getText().toString();

        if (hoursSlept.isEmpty()) {
            Toast.makeText(this, "Please enter hours slept", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "/" + userId;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("hoursSlept", Double.parseDouble(hoursSlept));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SleepActivity.this, "Sleep entry added successfully!", Toast.LENGTH_SHORT).show();
                        fetchSleepHistory(); // Refresh sleep history
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


    private void updateSleepEntry(int id) {
        String hoursSlept = sleepHoursInput.getText().toString();

        if (hoursSlept.isEmpty()) {
            Toast.makeText(this, "Please enter hours slept for update", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "/" + userId + "/" + id;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("hoursSlept", Double.parseDouble(hoursSlept));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SleepActivity.this, "Sleep entry updated successfully!", Toast.LENGTH_SHORT).show();
                        fetchSleepHistory(); // Refresh sleep history
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

    private void deleteSleepEntry(int id) {
        String url = BASE_URL + "/" + userId + "/" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SleepActivity.this, "Sleep entry deleted successfully!", Toast.LENGTH_SHORT).show();
                        fetchSleepHistory(); // Refresh sleep history
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

    private void getSleepAssessment() {
        String url = BASE_URL + "/" + userId + "/assessment";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SleepActivity.this, response, Toast.LENGTH_LONG).show();
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
