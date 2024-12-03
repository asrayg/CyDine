package CyDine.DiningHallMealPlan;

import CyDine.DiningHall.DiningHall;
import CyDine.DiningHall.DiningHallRepository;
import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import CyDine.Scraper.Scraper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@Tag(name = "Dining Hall Meal Plans", description = "Dining Hall Meal Plan management APIs")
public class DiningHallMealPlanController {

    @Autowired
    private DiningHallMealPlanRepository diningHallMealPlanRepository;

    @Autowired
    DiningHallRepository foodItemsRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get All Dining Hall Meal Plans", description = "Retrieves a list of all dining hall meal plans.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DiningHallMealPlan.class)))
    @GetMapping(path = "/DHmealplans")
    List<DiningHallMealPlan> getAllMealPlans() {
        return diningHallMealPlanRepository.findAll();
    }

    @Operation(summary = "Get Dining Hall Meal Plan by ID", description = "Retrieves a specific meal plan by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DiningHallMealPlan.class)))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @GetMapping(path = "/DHmealplans/{id}")
    DiningHallMealPlan getMealPlanById(@Parameter(description = "ID of the meal plan") @PathVariable int id) {
        return diningHallMealPlanRepository.findById(id);
    }

    @Operation(summary = "Create Dining Hall Meal Plan", description = "Creates a new meal plan with default values.")
    @ApiResponse(responseCode = "201", description = "Meal plan created",
            content = @Content(schema = @Schema(type = "integer", example = "2")))
    @PostMapping(path = "/DHmealplans")
    int createMealPlan() {
        DiningHallMealPlan mealPlan = new DiningHallMealPlan();
        DiningHallMealPlan mp = diningHallMealPlanRepository.save(mealPlan);
        return mp.getId();
    }


    @Operation(summary = "Create Dining Hall Meal Plan with AI", description = "Creates a new meal plan using AI")
    @ApiResponse(responseCode = "201", description = "Meal plan created",
            content = @Content(schema = @Schema(type = "integer", example = "2")))
    @PostMapping(path = "/DHmealplans/ai")
    int AiMealPlan() throws IOException {
        DiningHallMealPlan mealP = new DiningHallMealPlan();
        DiningHallMealPlan mp = diningHallMealPlanRepository.save(mealP);
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(mp.getId());

        String tmp = new JSONObject(new Scraper().ai()).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        if(tmp.charAt(0) == '`'){
            tmp = tmp.substring(7, tmp.length() - 3);
        }
        System.out.println(tmp);
        JSONObject foods = new JSONObject(tmp);
        System.out.println(foods);
        for(int i =0; i < foods.getJSONArray("Breakfast").length(); i++){
            DiningHall tmp2 = foodItemsRepository.findById(foods.getJSONArray("Breakfast").getJSONObject(i).getInt("id"));
            mealPlan.addFoodItem(tmp2);
        }
        for(int i =0; i < foods.getJSONArray("Lunch").length(); i++){
            DiningHall tmp2 = foodItemsRepository.findById(foods.getJSONArray("Breakfast").getJSONObject(i).getInt("id"));
            mealPlan.addFoodItem(tmp2);
        }
        for(int i =0; i < foods.getJSONArray("Dinner").length(); i++){
            DiningHall tmp2 = foodItemsRepository.findById(foods.getJSONArray("Breakfast").getJSONObject(i).getInt("id"));
            mealPlan.addFoodItem(tmp2);
        }
        DiningHallMealPlan mp2 = diningHallMealPlanRepository.save(mealPlan);
        return mp2.getId();
    }

    @Operation(summary = "Get Date of Meal Plan", description = "Retrieves the date associated with a specific meal plan.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "string", example = "2024-11-04T02:06:30.000+00:00")))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @GetMapping(path = "/DHmealplans/{id}/date")
    String getDate(@Parameter(description = "ID of the meal plan") @PathVariable int id) {
        return diningHallMealPlanRepository.findById(id).getDate().toString();
    }

    @Operation(summary = "Add Food Item to Meal Plan by Name", description = "Adds one or more food items to a meal plan based on their names.")
    @ApiResponse(responseCode = "200", description = "Food items added successfully",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\": \"success\"}")))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/DHmealplans/{id}/fooditems/add/byName")
    String addFoodItemToMealPlanByName(@Parameter(description = "ID of the meal plan") @PathVariable int id,
                                       @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        System.out.println("??????????????????????????????????");
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            System.out.println(i);
            for(DiningHall x : foodItemsRepository.findAll()){
                if (x.getName().equalsIgnoreCase(i) && x.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.now())){
                    System.out.println("SLDKF");
                    System.out.println("1");
                    mealPlan.addFoodItem(foodItemsRepository.findById(x.getId()));
                    break;

                }
            }
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Remove Food Item from Meal Plan by Name", description = "Removes one or more food items from a meal plan based on their names.")
    @ApiResponse(responseCode = "200", description = "Food items removed successfully",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\": \"success\"}")))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/DHmealplans/{id}/fooditems/remove/byName")
    String removeFoodItemFromMealPlanByName(@Parameter(description = "ID of the meal plan") @PathVariable int id,
                                            @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            for(DiningHall x : foodItemsRepository.findAll()){
                if (x.getName().equalsIgnoreCase(i)){
                    mealPlan.removeFoodItem(foodItemsRepository.findById(x.getId()));
                    break;
                }
            }
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Add Food Item to Meal Plan by ID", description = "Adds one or more food items to a meal plan based on their IDs.")
    @ApiResponse(responseCode = "200", description = "Food items added successfully",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\": \"success\"}")))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/DHmealplans/{id}/fooditems/add/byId")
    String addFoodItemToMealPlanById(@Parameter(description = "ID of the meal plan") @PathVariable int id,
                                     @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){

            mealPlan.addFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Remove Food Item from Meal Plan by ID", description = "Removes one or more food items from a meal plan based on their IDs.")
    @ApiResponse(responseCode = "200", description = "Food items removed successfully",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\": \"success\"}")))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping(path = "/DHmealplans/{id}/fooditems/remove/byId")
    String removeFoodItemFromMealPlanById(@Parameter(description = "ID of the meal plan") @PathVariable int id,
                                          @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            mealPlan.removeFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }

    @Operation(summary = "Delete Dining Hall Meal Plan", description = "Deletes a dining hall meal plan by its ID.")
    @ApiResponse(responseCode = "200", description = "Meal plan deleted successfully",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\": \"success\"}")))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @DeleteMapping(path = "/DHmealplans/{id}")
    String deleteMealPlan(@Parameter(description = "ID of the meal plan to delete") @PathVariable int id) {
        if (diningHallMealPlanRepository.existsById(id)) {
            System.out.println("????");
            for (DiningHall fi : diningHallMealPlanRepository.findById(id).getFoodItems()) {
                System.out.println(fi.getName());
                diningHallMealPlanRepository.findById(id).removeFoodItem(fi);
            }
            diningHallMealPlanRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @Operation(summary = "Update Dining Hall Meal Plan by Repeating Food", description = "Adds an existing food item to a meal plan, incrementing nutritional values accordingly.")
    @ApiResponse(responseCode = "200", description = "Meal plan updated successfully",
            content = @Content(schema = @Schema(implementation = DiningHallMealPlan.class)))
    @ApiResponse(responseCode = "404", description = "Meal plan or food item not found")
    @PutMapping("/DHmealplans/{id}/eatfoodagain/{foodid}")
    DiningHallMealPlan updateMealPlan(@Parameter(description = "ID of the meal plan") @PathVariable int id,
                                      @Parameter(description = "ID of the food item to repeat") @PathVariable int foodid) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealPlan.eatFoodAgain(foodItemsRepository.findById(foodid));
        diningHallMealPlanRepository.save(mealPlan);
        return diningHallMealPlanRepository.findById(id);
    }

    @Operation(summary = "Update Meal Plan", description = "Updates the details of an existing meal plan.")
    @ApiResponse(responseCode = "200", description = "Meal plan updated successfully",
            content = @Content(schema = @Schema(implementation = DiningHallMealPlan.class)))
    @ApiResponse(responseCode = "404", description = "Meal plan not found")
    @PutMapping("/DHmealplans/{id}")
    DiningHallMealPlan updateMealPlan(@Parameter(description = "ID of the meal plan to update") @PathVariable int id,
                                      @RequestBody DiningHallMealPlan request) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        diningHallMealPlanRepository.save(request);
        return diningHallMealPlanRepository.findById(id);
    }
}
