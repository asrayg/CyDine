package com.example.androidexample;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Map<String, String> headers;
    private final Map<String, DataPart> byteData;
    private final Response.Listener<NetworkResponse> listener;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.headers = new HashMap<>();
        this.byteData = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }


    protected Map<String, DataPart> getByteData() {
        return byteData;
    }

    public void addByteData(String key, DataPart data) {
        byteData.put(key, data);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Write multipart data
            for (Map.Entry<String, DataPart> entry : getByteData().entrySet()) {
                buildPart(bos, entry.getKey(), entry.getValue());
            }
            bos.write(("--" + boundary + "--").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    private void buildPart(ByteArrayOutputStream bos, String key, DataPart dataPart) throws IOException {
        bos.write(("--" + boundary + "\r\n").getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + dataPart.getFileName() + "\"\r\n").getBytes());
        bos.write(("Content-Type: " + dataPart.getType() + "\r\n\r\n").getBytes());
        bos.write(dataPart.getData());
        bos.write("\r\n".getBytes());
    }

    // Inner DataPart class to hold file details
    public static class DataPart {
        private final String fileName;
        private final byte[] data;
        private final String type;

        public DataPart(String fileName, byte[] data, String type) {
            this.fileName = fileName;
            this.data = data;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getData() {
            return data;
        }

        public String getType() {
            return type;
        }
    }

    private static final String boundary = "apiclient-" + System.currentTimeMillis();
}
