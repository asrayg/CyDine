package CyDine.Scraper;

import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scraper {
    public String[] getSlugs(){
        String urlString = "https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-all-locations"; // Replace with your URL
        JSONArray json = getJson(urlString);
        String[] slugs = new String[json.length()];
        for(int i = 0; i < json.length();i++){
            slugs[i] = json.getJSONObject(i).getString("slug");
        }
        System.out.println(Arrays.toString(slugs));
        return slugs;
    }

    public HashMap<String,JSONArray> getPlaces(){
        String[] slugs = getSlugs();
        HashMap<String,JSONArray> places = new HashMap<>();
        for(int i = 0; i < slugs.length;i++){
            places.put(slugs[i],getJson("https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-single-location/?slug=" + slugs[i]));
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

    @Scheduled(cron = "0 7 0 * * ?")
    public void getEachFood() throws IOException {
        HashMap<String, JSONArray> tmp = new Scraper().getPlaces();
        String[] places = {"seasons-marketplace-2-2","friley-windows-2-2","union-drive-marketplace-2-2"};
//       what the indexes in getJbj calls that are after get array calls can be
//        1: must be 0
//        2: can be multiple based on how many sections the dining hall has
//        3: must be 0 I think idk
//        4: this one is for each food item
        for (int z = 0; z < places.length; z++) {
            JSONArray pt1 = tmp.get(places[z]).getJSONObject(0).getJSONArray("menus");
            for (int i = 0; i < pt1.length(); i++) {
                JSONArray pt2 = pt1.getJSONObject(i).getJSONArray("menuDisplays").getJSONObject(0).getJSONArray("categories");
                for (int j = 0; j < pt2.length(); j++) {
                    System.out.println(pt2.length());
                    JSONArray pt3 = pt2.getJSONObject(j).getJSONArray("menuItems");
                    for (int k = 0; k < pt3.length(); k++) {
                        String name = pt3.getJSONObject(k).getString("name");
                        String totalCal = pt3.getJSONObject(k).getString("totalCal");
                        String nutrients = pt3.getJSONObject(k).getString("nutrients");
                        JSONArray nutrients2 = new JSONArray(nutrients);
                        if (!nutrients2.isEmpty()) {
                            String url = "http://127.0.0.1:8080/Dininghall";
                            String jsonInputString = "{" +
                                    "\"name\": \"" + name.replace(" ", "_") + "\"," +
                                    " \"protein\": " + nutrients2.getJSONObject(1).getString("qty") + "," +
                                    " \"carbs\": " + nutrients2.getJSONObject(1).getString("qty") + "," +
                                    " \"fat\": " + nutrients2.getJSONObject(1).getString("qty") + "," +
                                    " \"calories\": " + totalCal + "," +
                                    " \"dininghall\": \"" + places[z] + "\"," +
                                    " \"time\": \"" + pt1.getJSONObject(i).getString("section") + "\"" +
                                    "}";

                            URL obj = new URL(url);
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                            con.setRequestMethod("POST");
                            con.setRequestProperty("Content-Type", "application/json");
                            con.setDoOutput(true);

                            try (OutputStream os = con.getOutputStream()) {
                                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                                os.write(input, 0, input.length);
                            }
                        }
                    }
                }

            }
        }

        String webhookUrl = "https://canary.discord.com/api/webhooks/1303302400148508672/cuqOBt05Q7Wqfw85_C17wot97VY4nF5DPUKyhe1ofdo8hJeMFa-A5bPFMQpPHqkQ0OJx";

        JSONObject json = new JSONObject("{ \"content\" : \"" + "<@849437503181815878> hello" + "\"  }");

        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


