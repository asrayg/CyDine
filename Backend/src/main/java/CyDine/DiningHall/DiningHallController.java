package CyDine.DiningHall;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DiningHallController {

    @Autowired
    DiningHallRepository foodItemsRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/Dininghall")
    List<DiningHall> getAllFoods() {
        return foodItemsRepository.findAll();
    }

    @GetMapping(path = "/Dininghall/{id}")
    DiningHall getFoodsById(@PathVariable int id) {
        return foodItemsRepository.findById(id);
    }

    @PostMapping(path = "/Dininghall")
    int createFood(@RequestBody DiningHall food) {
        return foodItemsRepository.save(food).getId();
    }

    @Transactional
    @DeleteMapping(path = "/Dininghall/{id}")
    String deleteFood(@PathVariable int id) {
        if (foodItemsRepository.findById(id) != null) {
            foodItemsRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/Dininghall/{id}")
    DiningHall updateFood(@PathVariable int id, @RequestBody DiningHall request) {
        DiningHall foodItem = foodItemsRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        foodItemsRepository.save(request);
        return foodItemsRepository.findById(id);
    }


}
