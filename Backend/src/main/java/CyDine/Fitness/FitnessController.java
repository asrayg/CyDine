package CyDine.Fitness;

import CyDine.Maps.FoodPlace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Fitness", description = "Fitness management APIs")
public class FitnessController {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fitness_id")
    private List<Fitness> fitness;

    @ManyToOne
    private CyDine.Fitness.FitnessController Fitness;

    @Autowired
    FitnessRepository fitnessRepository;

    @Autowired
    StreakService streakService;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/fitness")
    @Operation(summary = "Get all fitness records", description = "Retrieves a list of all fitness records.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Fitness.class)))
    List<Fitness> getAllFitness() {
        return fitnessRepository.findAll();
    }

    @GetMapping(path = "/users/{userId}/fitness")
    @Operation(summary = "Get user's fitness records", description = "Retrieves all fitness records for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Fitness.class)))
    List<Fitness> getAllFitnessForUser(@Parameter(description = "ID of the user") @PathVariable int userId) {
        return fitnessRepository.findAll().stream()
                .filter(f -> f.getUserId() == userId)
                .toList();
    }

    @GetMapping(path = "/fitness/{id}")
    @Operation(summary = "Get fitness record by ID", description = "Retrieves a specific fitness record by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Fitness.class)))
    @ApiResponse(responseCode = "404", description = "Fitness record not found")
    Fitness getFitnessById(@Parameter(description = "ID of the fitness record") @PathVariable int id) {
        return fitnessRepository.findById(id);
    }

    @PostMapping(path = "/fitness")
    @Operation(summary = "Create fitness record", description = "Creates a new fitness record.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Fitness.class)))
    Fitness createFitness(@RequestBody Fitness fitness) {
        fitness.setActivity(fitness.getName());
        fitness.setDuration(fitness.getTime());
        fitness.setCaloriesBurned(fitness.getCalories());

        Fitness savedFitness = fitnessRepository.save(fitness);

        // Calculate and update streak
        int newStreak = streakService.calculateStreak(fitness.getUserId());
        savedFitness.setCurrentStreak(newStreak);
        return fitnessRepository.save(savedFitness);    }

    @DeleteMapping(path = "/fitness/{id}")
    @Operation(summary = "Delete fitness record", description = "Deletes a fitness record by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "404", description = "Fitness record not found")
    String deleteFitness(@Parameter(description = "ID of the fitness record to delete") @PathVariable int id) {
        if (fitnessRepository.findById(id) != null) {
            fitnessRepository.deleteById(id);
            return success;
        }
        return failure;    }

    @GetMapping(path = "/users/{userId}/streak")
    @Operation(summary = "Get user's fitness streak", description = "Retrieves the current fitness streak for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "integer")))
    int getUserStreak(@Parameter(description = "ID of the user") @PathVariable int userId) {
        return streakService.calculateStreak(userId);
    }

    @PutMapping("/fitness/{id}")
    @Operation(summary = "Update fitness record", description = "Updates an existing fitness record.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Fitness.class)))
    @ApiResponse(responseCode = "404", description = "Fitness record not found")
    Fitness updateFitness(@Parameter(description = "ID of the fitness record to update") @PathVariable int id,
                          @RequestBody Fitness request) {
        Fitness fitness = fitnessRepository.findById(id);
        if (fitness == null) {
            throw new RuntimeException("Fitness id does not exist");
        }
        request.setId(id);
        return fitnessRepository.save(request);
    }

    public List<CyDine.Fitness.Fitness> getFitness() {
        return fitness;
    }

    public void setFitness(FitnessController fitness) {
        Fitness = fitness;
    }


}
