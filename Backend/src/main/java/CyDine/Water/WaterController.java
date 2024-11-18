package CyDine.Water;

import CyDine.FoodItems.FoodItems;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Water", description = "Water intake management APIs")
public class WaterController {

    @Autowired
    WaterRepository waterRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ManyToOne
    @JsonIgnore
    private CyDine.Users.User user;

    @OneToMany
    @JsonIgnore
    private CyDine.MealPlans.MealPlans mealplan;

    @GetMapping(path = "/water")
    @Operation(summary = "Get all water records", description = "Retrieves a list of all water intake records.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    List<Water> getAllWaters() {
        return waterRepository.findAll();
    }

    @GetMapping(path = "/users/{userId}/water")
    @Operation(summary = "Get user's water records", description = "Retrieves all water intake records for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    List<Water> getAllWatersForUser(@Parameter(description = "ID of the user") @PathVariable int userId) {
        List<Water> tmp = new ArrayList<>();
        for (Water t: waterRepository.findAll()){
            if (t.getUserId() == userId){
                tmp.add(t);
            }
        }
        return tmp;
    }

    @GetMapping(path = "/users/{userId}/water/today")
    @Operation(summary = "Get user's today water record", description = "Retrieves the most recent water intake record for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    Water getTodaysWatersForUser(@Parameter(description = "ID of the user") @PathVariable int userId) {
        List<Water> tmp = new ArrayList<>();
        for (Water t: waterRepository.findAll()){
            if (t.getUserId() == userId){
                tmp.add(t);
            }
        }
        Water tmp2 = tmp.get(0);
        for (Water t: tmp){
            if(t.getDate().after(tmp2.getDate())){
                tmp2 = t;
            }
        }
        return tmp2;    }

    @GetMapping(path = "/water/{id}")
    @Operation(summary = "Get water record by ID", description = "Retrieves a specific water intake record by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    @ApiResponse(responseCode = "404", description = "Water record not found")
    Water getWaterById(@Parameter(description = "ID of the water record") @PathVariable int id) {
        return waterRepository.findById(id);
    }

    @PostMapping(path = "/water")
    @Operation(summary = "Create water record", description = "Creates a new water intake record.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "integer", example = "1")))
    int createWaterDate(@RequestBody Water water) {
        int tmp = waterRepository.save(water).getId();
        return tmp ;
    }

    @Transactional
    @DeleteMapping(path = "/water/{id}")
    @Operation(summary = "Delete water record", description = "Deletes a water intake record by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\":\"success\"}")))
    @ApiResponse(responseCode = "404", description = "Water record not found")
    String deleteWater(@Parameter(description = "ID of the water record to delete") @PathVariable int id) {
        if (waterRepository.findById(id) != null) {
            waterRepository.deleteById(id);
            return success;
        }
        return failure;    }

    @PutMapping("/water/{id}")
    @Operation(summary = "Update water record", description = "Updates an existing water intake record.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    @ApiResponse(responseCode = "404", description = "Water record not found")
    Water updateWater(@Parameter(description = "ID of the water record to update") @PathVariable int id, @RequestBody Water request) {
        Water foodItem = waterRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        waterRepository.save(request);
        return waterRepository.findById(id);    }

    @PutMapping("/users/{userId}/water/today/goal/{goal}")
    @Operation(summary = "Update today's water goal", description = "Updates the water intake goal for today for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    Water updateTodaysGoal(@Parameter(description = "ID of the user") @PathVariable int userId,
                           @Parameter(description = "New water intake goal") @PathVariable int goal) {
        List<Water> tmp = new ArrayList<>();
        for (Water t: waterRepository.findAll()){
            if (t.getUserId() == userId){
                tmp.add(t);
            }
        }
        Water tmp2 = tmp.get(0);
        for (Water t: tmp){
            if(t.getDate().after(tmp2.getDate())){
                tmp2 = t;
            }
        }
        tmp2.addToGoal(goal);
        waterRepository.save(tmp2);
        return tmp2;    }

    @PutMapping("/users/{userId}/water/today/drank/{amount}")
    @Operation(summary = "Add to today's water intake", description = "Adds to the water intake amount for today for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    Water addToDrankToday(@Parameter(description = "ID of the user") @PathVariable int userId,
                          @Parameter(description = "Amount of water to add") @PathVariable int amount) {
        List<Water> tmp = new ArrayList<>();
        for (Water t: waterRepository.findAll()){
            if (t.getUserId() == userId){
                tmp.add(t);
            }
        }
        Water tmp2 = tmp.get(0);
        for (Water t: tmp){
            if(t.getDate().after(tmp2.getDate())){
                tmp2 = t;
            }
        }
        waterRepository.save(tmp2);
        tmp2.addToTotal(amount);
        waterRepository.save(tmp2);
        return tmp2;    }

    @PutMapping("/water/{id}/{amount}")
    @Operation(summary = "Add to water intake", description = "Adds to the water intake amount for a specific water record.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Water.class)))
    @ApiResponse(responseCode = "404", description = "Water record not found")
    Water addToDrank(@Parameter(description = "ID of the water record") @PathVariable int id,
                     @Parameter(description = "Amount of water to add") @PathVariable int amount) {
        Water water = waterRepository.findById(id);
        if (water == null) {
            throw new RuntimeException("food id does not exist");
        }
        waterRepository.findById(id).addToTotal(amount);
        return waterRepository.findById(id);    }
}
