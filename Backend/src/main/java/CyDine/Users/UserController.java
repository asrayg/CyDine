package CyDine.Users;

import java.util.List;
import CyDine.DiningHallMealPlan.DiningHallMealPlan;
import CyDine.DiningHallMealPlan.DiningHallMealPlanRepository;
import CyDine.FoodItems.FoodItems;
import CyDine.MealPlans.MealPlans;
import CyDine.MealPlans.MealPlansRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MealPlansRepository mealPlansRepository;

    @Autowired
    DiningHallMealPlanRepository diningHallMealPlanRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    @Operation(summary = "Get All Users", description = "Retrieves a list of all users.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = User.class)))
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieves a user by their ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    User getUserById(@Parameter(description = "ID of the user to retrieve") @PathVariable int id) {
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    @Operation(summary = "Create User", description = "Creates a new user.")
    @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = String.class)))
    String createUser(@RequestBody User user) {
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update User by ID", description = "Updates an existing user by ID.")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    User updateUser(@Parameter(description = "ID of the user to update") @PathVariable int id, @RequestBody User request) {
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/users/{userId}/mealplan/{mealPlanId}")
    @Operation(summary = "Assign Meal Plan to User", description = "Assigns a meal plan to a user.")
    @ApiResponse(responseCode = "200", description = "Meal plan assigned successfully",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "User or meal plan not found")
    String assignMealPlan(@Parameter(description = "ID of the user") @PathVariable int userId,
                          @Parameter(description = "ID of the meal plan") @PathVariable int mealPlanId) {
        User user = userRepository.findById(userId);
        MealPlans mealPlan = mealPlansRepository.findById(mealPlanId);
        if(user == null || mealPlan == null)
            return failure;
        mealPlan.setUser(user);
        user.addMealPlans(mealPlan);
        userRepository.save(user);
        return success;
    }

    @Transactional
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete User", description = "Deletes a user.")
    @ApiResponse(responseCode = "200", description = "User deleted successfully",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    String deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable int id) {
        User user = userRepository.findById(id);
        if(user == null)
            return failure;
        userRepository.deleteById(id);
        return success;
    }

    @DeleteMapping(path = "/users/{id}/mealplan/{mealPlanId}")
    @Operation(summary = "Delete Meal Plan from User", description = "Removes a meal plan from a user.")
    @ApiResponse(responseCode = "200", description = "Meal plan removed successfully",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "User or meal plan not found")
    String deleteMealPlan(@Parameter(description = "ID of the user") @PathVariable int id,
                          @Parameter(description = "ID of the meal plan to remove") @PathVariable int mealPlanId) {
        userRepository.findById(id).removeMealPlans(mealPlansRepository.findById(mealPlanId));
        mealPlansRepository.deleteById(mealPlanId);
        return success;
    }

    @PutMapping("/users/password/")
    @Operation(summary = "Change User Password", description = "Changes the password of an existing user.")
    @ApiResponse(responseCode = "200", description = "Password changed successfully",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    String changePassword(@RequestBody UserPass request) {
        User user = userRepository.findByemailId(request.getEmailId());
        if(user == null)
            return failure;
        user.setPassword(request.getPassword());
        System.out.println(request.getPassword());
        userRepository.save(user);
        return success;
    }
}
