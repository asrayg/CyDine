package CyDine.Maps;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/foodplaces")
public class FoodPlaceController {

    @Autowired
    private FoodPlaceRepository foodPlaceRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    String createFoodPlace(@RequestBody FoodPlace foodPlace) {
        if (foodPlace.getRating() < 0 || foodPlace.getRating() > 5) {
            return "{\"message\":\"Rating must be between 0 and 5\"}";
        }

        if (foodPlace.getReview() == null || foodPlace.getReview().trim().isEmpty()) {
            return "{\"message\":\"Review cannot be empty\"}";
        }
        String imageUrl = generateImageUrl(foodPlace.getAddress(), foodPlace.getName());
        foodPlace.setImageUrl(imageUrl);

        foodPlaceRepository.save(foodPlace);
        return success;
    }

    @PutMapping("/{id}")
    String updateFoodPlace(@PathVariable int id, @RequestBody FoodPlace updatedFoodPlace) {
        FoodPlace foodPlace = foodPlaceRepository.findById(id);
        if (foodPlace == null) {
            return failure;
        }

        if (updatedFoodPlace.getRating() < 0 || updatedFoodPlace.getRating() > 5) {
            return "{\"message\":\"Rating must be between 0 and 5\"}";
        }

        if (updatedFoodPlace.getReview() == null || updatedFoodPlace.getReview().trim().isEmpty()) {
            return "{\"message\":\"Review cannot be empty\"}";
        }

        if (!foodPlace.getAddress().equals(updatedFoodPlace.getAddress()) ||
                !foodPlace.getName().equals(updatedFoodPlace.getName())) {
            String imageUrl = generateImageUrl(updatedFoodPlace.getAddress(), updatedFoodPlace.getName());
            updatedFoodPlace.setImageUrl(imageUrl);
        } else {
            updatedFoodPlace.setImageUrl(foodPlace.getImageUrl());
        }

        updatedFoodPlace.setId(id);
        foodPlaceRepository.save(updatedFoodPlace);
        return success;
    }

    public String generateImageUrl(String address, String name) {
        String imageUrl = "https://example.com/default-image.jpg"; // Default image URL
        try {
            String encodedAddress = URLEncoder.encode(address + ", " + name, StandardCharsets.UTF_8.toString());
            // Use Nominatim API to get coordinates for the address
            String nominatimUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress + "&limit=1";
            String response = restTemplate.getForObject(nominatimUrl, String.class);
            // Parse the JSON response to get latitude and longitude
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                double lat = firstResult.getDouble("lat");
                double lon = firstResult.getDouble("lon");

                // Generate the OpenStreetMap URL to display the address
                imageUrl = String.format(
                        "https://www.openstreetmap.org/?mlat=%f&mlon=%f#map=15/%f/%f",
                        lat, lon, lat, lon
                );
                System.out.println("Generated map URL: " + imageUrl); // Log the generated URL
            } else {
                System.out.println("No results found for the given address: " + address); // Log if no results are found
                // Provide a generic map centered on Ames, Iowa
                imageUrl = "https://www.openstreetmap.org/search?query=Mcdonald%27s%20Ames#map=19/42.021858/-93.611026";
            }
        } catch (Exception e) {
            System.out.println("Error generating map URL: " + e.getMessage()); // Log any exceptions
            e.printStackTrace();
        }
        return imageUrl;
    }
    @DeleteMapping("/{id}")
    String deleteFoodPlace(@PathVariable int id) {
        if (foodPlaceRepository.existsById(id)) {
            foodPlaceRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @GetMapping
    List<FoodPlace> getAllFoodPlaces() {
        return foodPlaceRepository.findAll();
    }

    @GetMapping("/{id}")
    FoodPlace getFoodPlaceById(@PathVariable int id) {
        return foodPlaceRepository.findById(id);
    }
}