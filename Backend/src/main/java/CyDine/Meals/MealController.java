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
public class MealController {

    @Autowired
    MealRepository mealRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/mealplans")
    List<MealPlans> getAllMealPlans() {
        return mealRepository.findAll();
    }

    @GetMapping(path = "/mealplans/{id}")
    MealPlans getMealPlanById(@PathVariable int id) {
        return mealRepository.findById(id);
    }

    @PostMapping(path = "/mealplans")
    String createMealPlan(@RequestBody MealPlans mealPlan) {
        if (mealPlan == null)
            return failure;
        mealRepository.save(mealPlan);
        return success;
    }

    @PostMapping(path = "/mealplans/{id}/fooditems")
    String addFoodItemToMealPlan(@PathVariable int id, @RequestBody FoodItems foodItem) {
        MealPlans mealPlan = mealRepository.findById(id);
        if (mealPlan == null)
            return failure;
        mealPlan.addFoodItem(foodItem);
        mealRepository.save(mealPlan);
        return success;
    }

    @DeleteMapping(path = "/mealplans/{id}")
    String deleteMealPlan(@PathVariable int id) {
        if (mealRepository.findById(id) != null) {
            mealRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/mealplans/{id}")
    MealPlans updateMealPlan(@PathVariable int id, @RequestBody MealPlans request) {
        MealPlans mealPlan = mealRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealRepository.save(request);
        return mealRepository.findById(id);
    }

}
