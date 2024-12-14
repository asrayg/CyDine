package CyDine.Scraper;

import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringEscapeUtils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scraper {
    public String[] getSlugs() {
        String urlString = "https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-all-locations"; // Replace with your URL
        JSONArray json = getJson(urlString);
        String[] slugs = new String[json.length()];
        for (int i = 0; i < json.length(); i++) {
            slugs[i] = json.getJSONObject(i).getString("slug");
        }
        System.out.println(Arrays.toString(slugs));
        return slugs;
    }

    public HashMap<String, JSONArray> getPlaces() {
        String[] slugs = getSlugs();
        HashMap<String, JSONArray> places = new HashMap<>();
        for (int i = 0; i < slugs.length; i++) {
            places.put(slugs[i], getJson("https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-single-location/?slug=" + slugs[i]));
        }
        return places;
    }


    private JSONArray getJson(String urlstr) {
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

    @Scheduled(cron = "0 7 0 * * *")
    public void getEachFood() throws IOException {
        HashMap<String, JSONArray> tmp = new Scraper().getPlaces();
        String[] places = {"seasons-marketplace-2-2", "friley-windows-2-2", "union-drive-marketplace-2-2"};
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
                            String url2 = "http://127.0.0.1:8080/Dininghall";
                            String jsonInputString = "{" +
                                    "\"name\": \"" + name.replace(" ", "_") + "\"," +
                                    " \"protein\": " + nutrients2.getJSONObject(1).getString("qty") + "," +
                                    " \"carbs\": " + nutrients2.getJSONObject(1).getString("qty") + "," +
                                    " \"fat\": " + nutrients2.getJSONObject(1).getString("qty") + "," +
                                    " \"calories\": " + totalCal + "," +
                                    " \"dininghall\": \"" + places[z] + "\"," +
                                    " \"time\": \"" + pt1.getJSONObject(i).getString("section") + "\"" +
                                    "}";

                            URL url = new URL(url2);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setDoOutput(true);

//                            String jsonPayload = "{\"content\":\"" + message + "\"}";

                            try (OutputStream os = connection.getOutputStream()) {
                                byte[] input = jsonInputString.getBytes("utf-8");
                                os.write(input, 0, input.length);
                            }

                            int responseCode = connection.getResponseCode();
                        }
                    }
                }

            }
        }


    }

    @Scheduled(cron = " 0 10-22 * * *")
    public void waterPing() {

        String usersUrl = "http://127.0.0.1:8080/users";
        try {
            URL url = new URL(usersUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response Code: " + responseCode);
            System.out.println("Response: " + response.toString());
            JSONArray json2 = new JSONArray(response.toString());

            for (int i = 0; i < json2.length(); i++) {
                int tmp = json2.getJSONObject(i).getInt("id");
                URL url2 = new URL(usersUrl + "/" + tmp + "/water/today");
                conn = (HttpURLConnection) url2.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                responseCode = conn.getResponseCode();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine2;
                response = new StringBuilder();

                while ((inputLine2 = in.readLine()) != null) {
                    response.append(inputLine2);
                }
                in.close();


                JSONObject json3 = new JSONObject(response.toString());
                int timeLeftInToday = 9 - LocalTime.now().getHour();
                int goal = json3.getInt("goal");
                int total = json3.getInt("total");
                if ((goal / 14) * timeLeftInToday < total || true) {
                    tmp("<@" + json2.getJSONObject(i).getString("discordUsername") + ">");
//                    System.out.println("test");
//                    tmp("<@" + "757272914633425027" + ">");

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void tmp(String message) throws IOException {
        String webhookUrl = "https://canary.discord.com/api/webhooks/1303302400148508672/cuqOBt05Q7Wqfw85_C17wot97VY4nF5DPUKyhe1ofdo8hJeMFa-A5bPFMQpPHqkQ0OJx";

        URL url = new URL(webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonPayload = "{\"content\":\"" + message + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
    }




    public String ai() throws IOException {
        String foodsUrl = "http://127.0.0.1:8080/Dininghall/today";
        URL url2 = new URL(foodsUrl);
        HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
        conn2.setRequestMethod("GET");
        conn2.setRequestProperty("Content-Type", "application/json");
        conn2.setDoOutput(true);

        int responseCode2 = conn2.getResponseCode();
        BufferedReader in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        String inputLine;
        StringBuilder response2 = new StringBuilder();

        while ((inputLine = in2.readLine()) != null) {
            response2.append(inputLine);
        }
        in2.close();

        System.out.println(response2);




        String foods = "I want you to make a meal plan using the foods i provide. Do not have any other text just the json. Make it in the format of a json object, here is an example of it" +
                " " +
                "{\n" +
                "    \"Breakfast\": [\n" +
                "      {\n" +
                "        \"id\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 7\n" +
                "      }\n" +
                "    ],\n" +
                "    \"Lunch\": [\n" +
                "        {\n" +
                "          \"id\": 5\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 22\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 117\n" +
                "        }\n" +
                "      ],\n" +
                "      \"Dinner\": [\n" +
                "        {\n" +
                "          \"id\": 20\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 12\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 75\n" +
                "        }\n" +
                "      ]\n" +
                "  }" +

                " " +
                "These are the foods" + response2;

        String urlString = "https://api.openai.com/v1/chat/completions";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set up the connection
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setDoOutput(true);

        String jsonInputString = "{"
                + "\"model\": \"gpt-4o\","
                + "\"messages\": ["
                + "{\"role\": \"user\", \"content\": \""+ StringEscapeUtils.escapeJava(foods) +"\"}"
                + "]"
                + "}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response: " + response.toString());
        if(response.charAt(0) == '\''){
            response = new StringBuilder(response.substring(6, response.length() - 3));
        }
        return String.valueOf(response);
    }

}
