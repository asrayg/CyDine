package CyDine.DiningHall;

import CyDine.FoodItems.FoodItems;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Tag(name = "Dining Hall", description = "Dining Hall management APIs")
public class DiningHallController {

    @Autowired
    DiningHallRepository diningHallRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/Dininghall")
    @Operation(summary = "Get all dining hall foods", description = "Retrieves a list of all dining hall foods.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DiningHall.class)))
    List<DiningHall> getAllFoods() {
        return diningHallRepository.findAll();
    }

    @GetMapping(path = "/Dininghall/{dininghall}/{time}")
    @Operation(summary = "Get dining hall foods by hall and time", description = "Retrieves dining hall foods for a specific hall and time on the current date.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DiningHall.class)))
    List<DiningHall> getDiningHallFoods(
            @Parameter(description = "Name of the dining hall") @PathVariable String dininghall,
            @Parameter(description = "Time of the meal") @PathVariable String time) {
        List<DiningHall> ret = new ArrayList<>();
        for(DiningHall t: diningHallRepository.findAll()){
            if (t.getDininghall().equals(dininghall) && t.getTime().equals(time) && t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.now())){
                ret.add(t);
            }
        }
        return ret;
    }

    @GetMapping(path = "/Dininghall/today")
    @Operation(summary = "Get dining hall foods for today", description = "Retrieves dining hall foods for a all halls and times on the current date.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DiningHall.class)))
    List<DiningHall> getAllDiningHallFoods() {
        List<DiningHall> ret = new ArrayList<>();
        for(DiningHall t: diningHallRepository.findAll()){
            if (t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.now())){
                ret.add(t);
            }
        }
        return ret;
    }

    @GetMapping(path = "/Dininghall/{id}")
    @Operation(summary = "Get dining hall food by ID", description = "Retrieves a specific dining hall food by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DiningHall.class)))
    @ApiResponse(responseCode = "404", description = "Dining hall food not found")
    DiningHall getFoodsById(@Parameter(description = "ID of the dining hall food") @PathVariable int id) {
        return diningHallRepository.findById(id);
    }

    @PostMapping(path = "/Dininghall")
    @Operation(summary = "Create dining hall food", description = "Creates a new dining hall food entry.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "integer")))
    int createFood(@RequestBody DiningHall food) {
        return diningHallRepository.save(food).getId();
    }

    @Transactional
    @DeleteMapping(path = "/Dininghall/{id}")
    @Operation(summary = "Delete dining hall food", description = "Deletes a dining hall food entry by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "404", description = "Dining hall food not found")
    String deleteFood(@Parameter(description = "ID of the dining hall food to delete") @PathVariable int id) {
        if (diningHallRepository.findById(id) != null) {
            diningHallRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/Dininghall/{id}")
    @Operation(summary = "Update dining hall food", description = "Updates an existing dining hall food entry.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DiningHall.class)))
    @ApiResponse(responseCode = "404", description = "Dining hall food not found")
    DiningHall updateFood(@Parameter(description = "ID of the dining hall food to update") @PathVariable int id,
                          @RequestBody DiningHall request) {
        DiningHall foodItem = diningHallRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        diningHallRepository.save(request);
        return diningHallRepository.findById(id);
    }
}
