package CyDine.Meditation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Meditation", description = "Meditation management APIs")
public class MeditationController {

    @Autowired
    MeditationRepository meditationRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/meditation")
    @Operation(summary = "Get all meditation records", description = "Retrieves a list of all meditation intake records.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Meditation.class)))
    List<Meditation> getAllMeditations() {
        return meditationRepository.findAll();
    }

    @GetMapping(path = "/users/{userId}/meditation")
    @Operation(summary = "Get user's meditation records", description = "Retrieves all meditation records for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Meditation.class)))
    List<Meditation> getAllMeditationsForUser(@Parameter(description = "ID of the user") @PathVariable int userId) {
        List<Meditation> tmp = new ArrayList<>();
        for (Meditation t: meditationRepository.findAll()){
            if (t.getUserId() == userId){
                tmp.add(t);
            }
        }
        return tmp;
    }

    @GetMapping(path = "/users/{userId}/meditation/today")
    @Operation(summary = "Get user's today meditation record", description = "Retrieves the meditation for today for a specific user.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Meditation.class)))
    List<Meditation> getTodaysMeditationsForUser(@Parameter(description = "ID of the user") @PathVariable int userId) {
        List<Meditation> tmp = new ArrayList<>();
        for (Meditation t: meditationRepository.findAll()){
            if (t.getUserId() == userId){
                tmp.add(t);
            }
        }
        List<Meditation> tmp2 = new ArrayList<>();
        for (Meditation t: tmp){
            if(t.getDate().isAfter(LocalDateTime.now().with(LocalTime.MIDNIGHT))){
                tmp2.add(t);
            }
        }
        return tmp2;
    }

    @GetMapping(path = "/meditation/{id}")
    @Operation(summary = "Get meditation record by ID", description = "Retrieves a specific meditation intake record by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Meditation.class)))
    @ApiResponse(responseCode = "404", description = "Meditation record not found")
    Meditation getMeditationById(@Parameter(description = "ID of the meditation record") @PathVariable int id) {
        return meditationRepository.findById(id);
    }

    @PostMapping(path = "/meditation")
    @Operation(summary = "Create meditation record", description = "Creates a new meditation record.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "integer", example = "1")))
    int createMeditation(@RequestBody Meditation meditation) {
        int tmp = meditationRepository.save(meditation).getId();
        return tmp ;
    }

    @Transactional
    @DeleteMapping(path = "/meditation/{id}")
    @Operation(summary = "Delete meditation record", description = "Deletes a meditation record by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "string", example = "{\"message\":\"success\"}")))
    @ApiResponse(responseCode = "404", description = "meditation record not found")
    String deleteMeditation(@Parameter(description = "ID of the meditation record to delete") @PathVariable int id) {
        if (meditationRepository.findById(id) != null) {
            meditationRepository.deleteById(id);
            return success;
        }
        return failure;   
    }

    @PutMapping("/meditation/{id}")
    @Operation(summary = "Update meditation record", description = "Updates an existing meditation intake record.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Meditation.class)))
    @ApiResponse(responseCode = "404", description = "Meditation record not found")
    Meditation updateMeditation(@Parameter(description = "ID of the meditation record to update") @PathVariable int id, @RequestBody Meditation request) {
        Meditation foodItem = meditationRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        meditationRepository.save(request);
        return meditationRepository.findById(id);
    }

}
