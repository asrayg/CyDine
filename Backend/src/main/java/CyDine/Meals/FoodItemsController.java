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

    @Autowired
    MealRepository mealRepository;

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

    @Transactional
    @PostMapping(path = "/FoodItem")
    int createFood(@RequestBody FoodItems food) {
        if (food == null)
            return -0;
        foodRepository.save(food);
        FoodItems savedFood = foodRepository.save(food);
        return savedFood.getId();
    }

    @Transactional
    @DeleteMapping(path = "/FoodItem/{id}")
    String deleteFood(@PathVariable int id) {
        if (foodRepository.findById(id) != null) {
            foodRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @Transactional
    @PutMapping("/FoodItem/{id}")
    FoodItems updateFood(@PathVariable int id, @RequestBody FoodItems request) {
        FoodItems foodItem = foodRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        foodItem.setCalories(request.getCalories());
        foodItem.setCarbs(request.getCarbs());
        foodItem.setFat(request.getFat());
        foodItem.setName(request.getName());
        foodItem.setProtein(request.getProtein());
        return foodRepository.findById(id);
    }

}

