package CyDine.SleepTracking;

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
@RequestMapping("/sleep")
@Tag(name = "Sleep", description = "Sleep tracking management APIs")
public class SleepController {

    @Autowired
    private SleepRepository sleepRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/{userId}")
    @Operation(summary = "Add new sleep entry", description = "Adds a new sleep entry for the user")
    @ApiResponse(responseCode = "200", description = "Sleep entry added successfully",
            content = @Content(schema = @Schema(implementation = SleepEntry.class)))
    public String addSleep(@Parameter(description = "ID of the user") @PathVariable int userId,
                           @RequestBody SleepEntry sleepEntry) {
        sleepEntry.setDate(LocalDate.now());
        sleepEntry.setUserId(userId);
        sleepRepository.save(sleepEntry);
        return success;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user's sleep history", description = "Retrieves all sleep entries for a specific user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = SleepEntry.class)))
    public List<SleepEntry> getUserSleepHistory(@Parameter(description = "ID of the user") @PathVariable int userId) {
        return sleepRepository.findByUserIdOrderByDateDesc(userId);
    }

    @GetMapping("/{userId}/latest")
    @Operation(summary = "Get user's latest sleep entry", description = "Retrieves the most recent sleep entry for a specific user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = SleepEntry.class)))
    public SleepEntry getLatestSleep(@Parameter(description = "ID of the user") @PathVariable int userId) {
        return sleepRepository.findTopByUserIdOrderByDateDesc(userId);
    }

    @GetMapping("/{userId}/assessment")
    @Operation(summary = "Get sleep assessment", description = "Assesses if the user's latest sleep duration is adequate")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "string")))
    public String getSleepAssessment(@Parameter(description = "ID of the user") @PathVariable int userId) {
        SleepEntry latestEntry = sleepRepository.findTopByUserIdOrderByDateDesc(userId);
        if (latestEntry == null) {
            return "No sleep data available";
        }

        double hoursSlept = latestEntry.getHoursSlept();
        if (hoursSlept >= 6 && hoursSlept <= 8) {
            return "You slept " + hoursSlept + " hours. This is an adequate amount of sleep.";
        } else if (hoursSlept < 6) {
            return "You slept " + hoursSlept + " hours. You need to get more sleep for better health.";
        } else {
            return "You slept " + hoursSlept + " hours. This is more than the recommended amount, but occasional oversleeping is generally not a concern.";
        }
    }
}