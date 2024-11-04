package CyDine.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodplaces")
public class FoodPlaceController {

    @Autowired
    private FoodPlaceRepository foodPlaceRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping
    List<FoodPlace> getAllFoodPlaces() {
        return foodPlaceRepository.findAll();
    }

    @GetMapping("/{id}")
    FoodPlace getFoodPlaceById(@PathVariable int id) {
        return foodPlaceRepository.findById(id);
    }

    @PostMapping
    String createFoodPlace(@RequestBody FoodPlace foodPlace) {
        if (foodPlace.getRating() < 0 || foodPlace.getRating() > 5) {
            return "{\"message\":\"Rating must be between 0 and 5\"}";
        }
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
        updatedFoodPlace.setId(id);
        foodPlaceRepository.save(updatedFoodPlace);
        return success;
    }

    @DeleteMapping("/{id}")
    String deleteFoodPlace(@PathVariable int id) {
        if (foodPlaceRepository.existsById(id)) {
            foodPlaceRepository.deleteById(id);
            return success;
        }
        return failure;
    }
}