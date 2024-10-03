package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class StringReqActivity extends AppCompatActivity {

    private Button btnStringReq;
    private Button btnStringPOSTReq;
    private TextView msgResponse;

    private static final String URL_STRING_REQ = "https://6de1b4f1-66e3-457b-8bd7-a939440434ff.mock.pstmn.io/get";
    private static final String URL_STRING_POST_REQ = "https://6de1b4f1-66e3-457b-8bd7-a939440434ff.mock.pstmn.io/create";
    //   public static final String URL_STRING_REQ = "https://2aa87adf-ff7c-45c8-89bc-f3fbfaa16d15.mock.pstmn.io/users/1";
    //   public static final String URL_STRING_REQ = "http://10.0.2.2:8080/users/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_req);

        btnStringReq = (Button) findViewById(R.id.btnStringReq);
        msgResponse = (TextView) findViewById(R.id.msgResponse);
        btnStringPOSTReq = (Button) findViewById(R.id.btnStringPOSTReq);

        btnStringReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeStringReq();
            }
        });
        btnStringPOSTReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePOSTReq();
            }
        });
    }

    private void makePOSTReq() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_STRING_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley POST Response", response);
                        msgResponse.append("\nPOST Response:\n" + response); // Append POST response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley POST Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            public byte[] getBody() {
                // Construct the JSON body with dynamic user inputs
                String requestBody = "{\"name\":\"Bekri Bekri\"," +
                        "\"email\":\"issmale@iastate.edu\"," +
                        "\"major\":\"Computer Science\"," +
                        "\"year\":\"Junior\"," +
                        "\"phone\":\"" + "23231" + "\"}"; // Inject the phone number
                return requestBody.getBytes();
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    /**
     * Making string request
     **/
    private void makeStringReq() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL_STRING_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        msgResponse.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                    }
                }
        );
//        {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
////                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
////                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
////                params.put("param1", "value1");
////                params.put("param2", "value2");
//                return params;
//            }
//        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}