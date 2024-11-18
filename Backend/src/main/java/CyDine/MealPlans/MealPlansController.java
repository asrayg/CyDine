package CyDine.MealPlans;

import java.util.ArrayList;
import java.util.List;
import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import CyDine.Users.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Meal Plans", description = "Meal Plans management APIs")
public class MealPlansController {

    @Autowired
    private MealPlansRepository mealPlansRepository;

    @Autowired
    FoodItemsRepository foodItemsRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all meal plans", description = "Retrieves a list of all meal plans")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = MealPlans.class)))
    @GetMapping(path = "/mealplans")
    List<MealPlans> getAllMealPlans() {
        return mealPlansRepository.findAll();
    }

    @Operation(summary = "Get meal plan by ID", description = "Retrieves a specific meal plan by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = MealPlans.class)))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @GetMapping(path = "/mealplans/{id}")
    MealPlans getMealPlanById(@Parameter(description = "ID of the meal plan") @PathVariable int id) {
        return mealPlansRepository.findById(id);
    }

    @Operation(summary = "Create meal plan", description = "Creates a new meal plan")
    @ApiResponse(responseCode = "201", description = "Meal plan created",
            content = @Content(schema = @Schema(implementation = Integer.class)))
    @PostMapping(path = "/mealplans")
    int createMealPlan() {
        MealPlans mealPlan = new MealPlans();
        MealPlans mp = mealPlansRepository.save(mealPlan);
        return mp.getId();
    }

    @Operation(summary = "Create meal plan with items", description = "Creates a new meal plan with food items")
    @ApiResponse(responseCode = "201", description = "Meal plan created",
            content = @Content(schema = @Schema(implementation = Integer.class)))
    @PostMapping(path = "/mealplans/additm")
    int createMealPlan2(@RequestBody String mealpln) {
        MealPlans mealPlan = new MealPlans();
        JSONObject job = new JSONObject(mealpln);
        for (String i : job.getString("foodItems").split(",")) {
            mealPlan.addFoodItem(foodItemsRepository.save(new FoodItems(i,0,0,0,0,0)));
        }
        mealPlan.addCarbs(job.getInt("carbs"));
        mealPlan.addProtein(job.getInt("protein"));
        mealPlan.addFat(job.getInt("fat"));
        mealPlan.addCals(job.getInt("finalCalories"));
        MealPlans mp = mealPlansRepository.save(mealPlan);
        return mp.getId();
    }

    @Operation(summary = "Get meal plan date", description = "Retrieves the date associated with a meal plan")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping(path = "/mealplans/{id}/date")
    String getDate(@Parameter(description = "ID of the meal plan") @PathVariable int id) {
        return mealPlansRepository.findById(id).getDate().toString();
    }

    @Operation(summary = "Add food items to meal plan by name", description = "Adds one or more food items to a meal plan by name")
    @ApiResponse(responseCode = "200", description = "Food items added successfully")
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/mealplans/{id}/fooditems/add/byName/{userId}")
    String addFoodItemToMealPlanByName(
            @Parameter(description = "ID of the meal plan") @PathVariable int id,
            @Parameter(description = "ID of the user") @PathVariable int userId,
            @Parameter(description = "Comma-separated list of food item names") @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")) {
            for(FoodItems x : foodItemsRepository.findAll()) {
                if (x.getName() != null) {
                    if (x.getName().equalsIgnoreCase(i)) {
                        if (x.getUserId() == userId) {
                            mealPlan.addFoodItem(foodItemsRepository.findById(x.getId()));
                            break;
                        }
                    }
                }
            }
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Remove food items from meal plan by name", description = "Removes one or more food items from a meal plan by name")
    @ApiResponse(responseCode = "200", description = "Food items removed successfully")
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/mealplans/{id}/fooditems/remove/byName")
    String removeFoodItemFromMealPlanByName(
            @Parameter(description = "ID of the meal plan") @PathVariable int id,
            @Parameter(description = "Comma-separated list of food item names") @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")) {
            for(FoodItems x : foodItemsRepository.findAll()) {
                if (x.getName().equalsIgnoreCase(i)) {
                    mealPlan.removeFoodItem(foodItemsRepository.findById(x.getId()));
                    break;
                }
            }
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Add food items to meal plan by ID", description = "Adds one or more food items to a meal plan by ID")
    @ApiResponse(responseCode = "200", description = "Food items added successfully")
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/mealplans/{id}/fooditems/add/byId")
    String addFoodItemToMealPlanById(
            @Parameter(description = "ID of the meal plan") @PathVariable int id,
            @Parameter(description = "Comma-separated list of food item IDs") @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")) {
            mealPlan.addFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Remove food items from meal plan by ID", description = "Removes one or more food items from a meal plan by ID")
    @ApiResponse(responseCode = "200", description = "Food items removed successfully")
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/mealplans/{id}/fooditems/remove/byId")
    String removeFoodItemFromMealPlanById(
            @Parameter(description = "ID of the meal plan") @PathVariable int id,
            @Parameter(description = "Comma-separated list of food item IDs") @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")) {
            mealPlan.removeFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Delete meal plan", description = "Deletes a meal plan")
    @ApiResponse(responseCode = "200", description = "Meal plan deleted successfully")
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @DeleteMapping(path = "/mealplans/{id}")
    String deleteMealPlan(@Parameter(description = "ID of the meal plan to delete") @PathVariable int id) {
        if (mealPlansRepository.existsById(id)) {
            for (FoodItems fi : mealPlansRepository.findById(id).getFoodItems()) {
                mealPlansRepository.findById(id).removeFoodItem(fi);
            }
            mealPlansRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @Operation(summary = "Eat food again", description = "Updates a meal plan by eating a food item again")
    @ApiResponse(responseCode = "200", description = "Meal plan updated successfully",
            content = @Content(schema = @Schema(implementation = MealPlans.class)))
    @ApiResponse(responseCode = "404", description = "Meal plan or food item not found")
    @PutMapping("/mealplans/{id}/eatfoodagain/{foodid}")
    MealPlans updateMealPlan(
            @Parameter(description = "ID of the meal plan") @PathVariable int id,
            @Parameter(description = "ID of the food item") @PathVariable int foodid) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealPlan.eatFoodAgain(foodItemsRepository.findById(foodid));
        mealPlansRepository.save(mealPlan);
        return mealPlansRepository.findById(id);
    }

    @Operation(summary = "Update meal plan", description = "Updates the details of an existing meal plan")
    @ApiResponse(responseCode = "200", description = "Meal plan updated successfully",
            content = @Content(schema = @Schema(implementation = MealPlans.class)))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping("/mealplans/{id}")
    MealPlans updateMealPlan(
            @Parameter(description = "ID of the meal plan to update") @PathVariable int id,
            @Parameter(description = "Updated meal plan details") @RequestBody MealPlans request) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealPlansRepository.save(request);
        return mealPlansRepository.findById(id);
    }
}
