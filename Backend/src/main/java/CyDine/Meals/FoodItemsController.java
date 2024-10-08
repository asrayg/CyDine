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

    @GetMapping(path = "/mealplans")
    List<MealPlans> getAllMealPlans() {
        return foodRepository.findAll();
    }

    @GetMapping(path = "/mealplans/{id}")
    MealPlans getMealPlanById(@PathVariable int id) {
        return foodRepository.findById(id);
    }

    @PostMapping(path = "/mealplans")
    String createMealPlan(@RequestBody MealPlans mealPlan) {
        if (mealPlan == null)
            return failure;
        foodRepository.save(mealPlan);
        return success;
    }

    @PostMapping(path = "/mealplans/{id}/fooditems")
    String addFoodItemToMealPlan(@PathVariable int id, @RequestBody FoodItems foodItem) {
        MealPlans mealPlan = foodRepository.findById(id);
        if (mealPlan == null)
            return failure;
        mealPlan.addFoodItems(foodItem);
        foodRepository.save(mealPlan);
        return success;
    }

    @DeleteMapping(path = "/mealplans/{id}")
    String deleteMealPlan(@PathVariable int id) {
        if (foodRepository.findById(id) != null) {
            foodRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/mealplans/{id}")
    MealPlans updateMealPlan(@PathVariable int id, @RequestBody MealPlans request) {
        MealPlans mealPlan = foodRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        foodRepository.save(request);
        return foodRepository.findById(id);
    }

}

