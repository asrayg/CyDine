package CyDine.Weight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/weight")
@Tag(name = "Weight", description = "Weight tracking management APIs")
public class WeightController {

    @Autowired
    private WeightRepository weightRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/{userId}")
    @Operation(summary = "Add new weight entry", description = "Adds a new weight entry for the user")
    @ApiResponse(responseCode = "200", description = "Weight entry added successfully",
            content = @Content(schema = @Schema(implementation = WeightEntry.class)))
    public String addWeight(@Parameter(description = "ID of the user") @PathVariable int userId,
                            @RequestBody WeightEntry weightEntry) {
        weightEntry.setDate(LocalDate.now());
        weightEntry.setUserId(userId);
        weightRepository.save(weightEntry);
        return success;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user's weight history", description = "Retrieves all weight entries for a specific user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = WeightEntry.class)))
    public List<WeightEntry> getUserWeightHistory(@Parameter(description = "ID of the user") @PathVariable int userId) {
        return weightRepository.findByUserIdOrderByDateDesc(userId);
    }

    @GetMapping("/{userId}/latest")
    @Operation(summary = "Get user's latest weight", description = "Retrieves the most recent weight entry for a specific user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = WeightEntry.class)))
    public WeightEntry getLatestWeight(@Parameter(description = "ID of the user") @PathVariable int userId) {
        return weightRepository.findTopByUserIdOrderByDateDesc(userId);
    }

    @GetMapping("/{userId}/progress")
    @Operation(summary = "Get user's weight progress", description = "Calculates the weight change since the first entry")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "string")))
    public String getWeightProgress(@Parameter(description = "ID of the user") @PathVariable int userId) {
        List<WeightEntry> entries = weightRepository.findByUserIdOrderByDateAsc(userId);
        if (entries.size() < 2) {
            return "Not enough data to calculate progress";
        }
        double initialWeight = entries.get(0).getWeight();
        double currentWeight = entries.get(entries.size() - 1).getWeight();
        double change = currentWeight - initialWeight;
        return String.format("Weight change: %.1f kg", change);
    }
    @PutMapping("/{userId}/{entryId}")
    @Operation(summary = "Update a weight entry", description = "Updates a specific weight entry for a user")
    @ApiResponse(responseCode = "200", description = "Weight entry updated successfully",
            content = @Content(schema = @Schema(implementation = WeightEntry.class)))
    public String updateWeightEntry(@Parameter(description = "ID of the user") @PathVariable int userId,
                                    @Parameter(description = "ID of the weight entry") @PathVariable int entryId,
                                    @RequestBody WeightEntry updatedEntry) {
        WeightEntry weightEntry = weightRepository.findById(entryId).orElse(null);
        if (weightEntry == null || weightEntry.getUserId() != userId) {
            return failure;
        }
        weightEntry.setWeight(updatedEntry.getWeight());
        weightEntry.setDate(updatedEntry.getDate());
        weightRepository.save(weightEntry);
        return success;
    }

    @DeleteMapping("/{userId}/{entryId}")
    @Operation(summary = "Delete a weight entry", description = "Deletes a specific weight entry for a user")
    @ApiResponse(responseCode = "200", description = "Weight entry deleted successfully",
            content = @Content(schema = @Schema(implementation = String.class)))
    public String deleteWeightEntry(@Parameter(description = "ID of the user") @PathVariable int userId,
                                    @Parameter(description = "ID of the weight entry") @PathVariable int entryId) {
        WeightEntry weightEntry = weightRepository.findById(entryId).orElse(null);
        if (weightEntry == null || weightEntry.getUserId() != userId) {
            return failure;
        }
        weightRepository.deleteById(entryId);
        return success;
    }
}