package CyDine.FoodItems;

import java.util.ArrayList;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Food Items", description = "Food Items management APIs")
public class FoodItemsController {

    @Autowired
    FoodItemsRepository foodItemsRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/FoodItem")
    @Operation(summary = "Get All Food Items", description = "Retrieves a list of all food items.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = FoodItems.class)))
    List<FoodItems> getAllFoods() {
        return foodItemsRepository.findAll();
    }

    @GetMapping(path = "/FoodItem/{id}")
    @Operation(summary = "Get Food Item by ID", description = "Retrieves a specific food item by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = FoodItems.class)))
    @ApiResponse(responseCode = "404", description = "Food item not found")
    FoodItems getFoodsById(@Parameter(description = "ID of the food item to retrieve") @PathVariable int id) {
        return foodItemsRepository.findById(id);
    }

    @PostMapping(path = "/FoodItem")
    @Operation(summary = "Create Food Item", description = "Creates a new food item.")
    @ApiResponse(responseCode = "201", description = "Food item created successfully",
            content = @Content(schema = @Schema(type = "integer", example = "3")))
    int createFood(@RequestBody FoodItems food) {
        return foodItemsRepository.save(food).getId();
    }

    @Transactional
    @DeleteMapping(path = "/FoodItem/{id}")
    @Operation(summary = "Delete Food Item", description = "Deletes a food item by its ID.")
    @ApiResponse(responseCode = "200", description = "Food item deleted successfully",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\":\"success\"}")))
    @ApiResponse(responseCode = "404", description = "Food item not found")
    String deleteFood(@Parameter(description = "ID of the food item to delete") @PathVariable int id) {
        if (foodItemsRepository.findById(id) != null) {
            foodItemsRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/FoodItem/{id}")
    @Operation(summary = "Update Food Item", description = "Updates the details of an existing food item.")
    @ApiResponse(responseCode = "200", description = "Food item updated successfully",
            content = @Content(schema = @Schema(implementation = FoodItems.class)))
    @ApiResponse(responseCode = "404", description = "Food item not found")
    FoodItems updateFood(@Parameter(description = "ID of the food item to update") @PathVariable int id,
                         @RequestBody FoodItems request) {
        FoodItems foodItem = foodItemsRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        foodItemsRepository.save(request);
        return foodItemsRepository.findById(id);
    }

    @GetMapping(path = "/users/{id}/FoodItems")
    @Operation(summary = "Get User's Food Items", description = "Retrieves all food items for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = FoodItems.class)))
    List<FoodItems> getUserFoodsById(@Parameter(description = "ID of the user") @PathVariable int id) {
        List<FoodItems> tmp = new ArrayList<>();
        for (FoodItems t : foodItemsRepository.findAll()) {
            if (t.getUserId() == id) {
                tmp.add(t);
            }
        }
        return tmp;
    }
}
