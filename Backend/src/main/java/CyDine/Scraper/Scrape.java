package CyDine.Scraper;


import org.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Scrape {
    public String[] getSlugs(){
        String urlString = "https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-all-locations"; // Replace with your URL
        JSONArray json = getJson(urlString);
        String[] slugs = new String[json.length()];
        for(int i = 0; i < json.length();i++){
            slugs[i] = json.getJSONObject(i).getString("slug");
        }
        return slugs;
    }

    public JSONArray[] getPlaces(){
        String[] slugs = getSlugs();
        JSONArray[] places = new JSONArray[slugs.length+1];
        for(int i = 0; i < slugs.length;i++){
            places[i] = getJson("https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-single-location/?slug=" + slugs[i]);
        }
        return places;
    }


    private JSONArray getJson(String urlstr){
        try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
            String jsonResponse = sb.toString();
            JSONArray json = new JSONArray(jsonResponse);
            return json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
