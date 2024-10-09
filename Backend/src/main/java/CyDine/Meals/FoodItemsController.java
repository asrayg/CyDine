package CyDine.Meals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodItemsController {

    @Autowired
    FoodRepository foodRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/FoodItem")
    List<FoodItems> getAllFoods() {
        return foodRepository.findAll();
    }

    @GetMapping(path = "/FoodItem/{id}")
    FoodItems getFoodsById(@PathVariable int id) {
        return foodRepository.findById(id);
    }

    @PostMapping(path = "/FoodItem")
    int createFood(@RequestBody FoodItems food) {
        if (food == null)
            return -0;
        foodRepository.save(food);
        FoodItems savedFood = foodRepository.save(food);
        return savedFood.getId();
    }

    @DeleteMapping(path = "/food/{id}")
    String deleteFood(@PathVariable int id) {
        if (foodRepository.findById(id) != null) {
            foodRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/food/{id}")
    FoodItems updateFood(@PathVariable int id, @RequestBody FoodItems request) {
        FoodItems foodItem = foodRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        foodRepository.save(request);
        return foodRepository.findById(id);
    }

}

