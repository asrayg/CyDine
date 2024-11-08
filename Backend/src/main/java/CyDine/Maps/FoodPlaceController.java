package CyDine.Maps;

import CyDine.FoodItems.FoodItems;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "foodPlace_id")
    private List<FoodPlace> foodPlace;

    @ManyToOne
    private CyDine.Maps.FoodPlaceController FoodPlace;

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
            // Encode the address and name into a search query
            String encodedAddress = URLEncoder.encode(name + " " + address, StandardCharsets.UTF_8.toString());

            // Use Nominatim API to get coordinates for the place
            String nominatimUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress + "&limit=1";
            String response = restTemplate.getForObject(nominatimUrl, String.class);

            // Parse the JSON response to extract the latitude and longitude
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                double lat = firstResult.getDouble("lat");
                double lon = firstResult.getDouble("lon");

                // Generate the OpenStreetMap URL using the retrieved coordinates
                imageUrl = String.format(
                        "https://www.openstreetmap.org/?mlat=%f&mlon=%f#map=15/%f/%f",
                        lat, lon, lat, lon
                );
            } else {
                // Provide a generic map centered on Ames, Iowa as a fallback
                imageUrl = "https://www.openstreetmap.org/search?query=Ames%2C%20Iowa#map=15/42.0220/-93.6110";
            }
        } catch (Exception e) {
            System.out.println("Error generating map URL: " + e.getMessage());
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

    public List<CyDine.Maps.FoodPlace> getFoodPlace() {
        return foodPlace;
    }
}