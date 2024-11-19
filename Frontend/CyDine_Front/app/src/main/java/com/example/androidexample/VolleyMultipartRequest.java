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

/**
 * A custom Volley request for handling multipart/form-data requests, typically used
 * for uploading files and other data to a server.
 */
public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Map<String, String> headers;
    private final Map<String, DataPart> byteData;
    private final Response.Listener<NetworkResponse> listener;

    /**
     * Constructor for creating a new VolleyMultipartRequest.
     *
     * @param method        The HTTP method to use (e.g., {@link Request.Method#POST}).
     * @param url           The URL to send the request to.
     * @param listener      The listener to handle the response.
     * @param errorListener The listener to handle errors.
     */
    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.headers = new HashMap<>();
        this.byteData = new HashMap<>();
    }

    /**
     * Gets the request headers.
     *
     * @return A map of headers to include in the request.
     */
    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }


    /**
     * Sets the request headers.
     *
     * @param headers A map of headers to include in the request.
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    /**
     * Gets the byte data for the request.
     *
     * @return A map of byte data to include in the request body.
     */
    protected Map<String, DataPart> getByteData() {
        return byteData;
    }

    /**
     * Adds a piece of byte data to the request.
     *
     * @param key  The key to identify the data part.
     * @param data The {@link DataPart} containing the file data.
     */
    public void addByteData(String key, DataPart data) {
        byteData.put(key, data);
    }

    /**
     * Parses the network response received from the server.
     *
     * @param response The raw network response.
     * @return A parsed response object.
     */
    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * Delivers the parsed response to the listener.
     *
     * @param response The parsed response object.
     */
    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    /**
     * Returns the content type of the request body.
     *
     * @return The content type, including the boundary.
     */
    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    /**
     * Generates the request body in multipart form.
     *
     * @return The byte array of the request body.
     * @throws AuthFailureError If an authentication failure occurs.
     */
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


    /**
     * Builds a part of the multipart request body.
     *
     * @param bos      The {@link ByteArrayOutputStream} to write data to.
     * @param key      The key identifying the data part.
     * @param dataPart The {@link DataPart} containing file data.
     * @throws IOException If an I/O error occurs.
     */
    private void buildPart(ByteArrayOutputStream bos, String key, DataPart dataPart) throws IOException {
        bos.write(("--" + boundary + "\r\n").getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + dataPart.getFileName() + "\"\r\n").getBytes());
        bos.write(("Content-Type: " + dataPart.getType() + "\r\n\r\n").getBytes());
        bos.write(dataPart.getData());
        bos.write("\r\n".getBytes());
    }


    /**
     * Inner class representing a part of the multipart request body.
     */
    public static class DataPart {
        private final String fileName;
        private final byte[] data;
        private final String type;

        /**
         * Constructor for creating a new DataPart.
         *
         * @param fileName The file name.
         * @param data     The byte data of the file.
         * @param type     The MIME type of the file.
         */
        public DataPart(String fileName, byte[] data, String type) {
            this.fileName = fileName;
            this.data = data;
            this.type = type;
        }

        /**
         * Gets the file name.
         *
         * @return The file name.
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * Gets the byte data of the file.
         *
         * @return The byte data.
         */
        public byte[] getData() {
            return data;
        }

        /**
         * Gets the MIME type of the file.
         *
         * @return The MIME type.
         */
        public String getType() {
            return type;
        }
    }

    private static final String boundary = "apiclient-" + System.currentTimeMillis();
}
