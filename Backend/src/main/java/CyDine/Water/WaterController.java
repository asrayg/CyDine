package CyDine.Water;

import CyDine.FoodItems.FoodItems;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WaterController {

    @Autowired
    WaterRepository waterRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/water")
    List<Water> getAllWaters() {
        return waterRepository.findAll();
    }

    @GetMapping(path = "/users/{userId}/water")
    List<Water> getAllWatersForUser(@PathVariable int userId) {
        List<Water> tmp = new ArrayList<>();
        for (Water t: waterRepository.findAll()){
            if (t.getUserId() == userId){
                tmp.add(t);
            }
        }
        return tmp;
    }

    @GetMapping(path = "/water/{id}")
    Water getWaterById(@PathVariable int id) {
        return waterRepository.findById(id);
    }

    @PostMapping(path = "/water")
    int createWaterDate(@RequestBody Water water) {
        return waterRepository.save(water).getId();
    }

    @Transactional
    @DeleteMapping(path = "/water/{id}")
    String deleteWater(@PathVariable int id) {
        if (waterRepository.findById(id) != null) {
            waterRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/water/{id}")
    Water updateWater(@PathVariable int id, @RequestBody Water request) {
        Water foodItem = waterRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        waterRepository.save(request);
        return waterRepository.findById(id);
    }

    @PutMapping("/water/{id}/{amount}")
    Water addToDrank(@PathVariable int id, @PathVariable int amount) {
        Water water = waterRepository.findById(id);
        if (water == null) {
            throw new RuntimeException("food id does not exist");
        }
        waterRepository.findById(id).addToTotal(amount);
        return waterRepository.findById(id);
    }

}
