package CyDine.DiningHall;

import CyDine.FoodItems.FoodItems;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class DiningHallController {

    @Autowired
    DiningHallRepository diningHallRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/Dininghall")
    List<DiningHall> getAllFoods() {
        return diningHallRepository.findAll();
    }

    @GetMapping(path = "/Dininghall/{dininghall}/{time}")
    List<DiningHall> getDiningHallFoods(@PathVariable String dininghall, @PathVariable String time) {
        List<DiningHall> ret = new ArrayList<>();
        for(DiningHall t: diningHallRepository.findAll()){
            if (t.getDininghall().equals(dininghall) && t.getTime().equals(time) && t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.now())){
                ret.add(t);
            }
        }
        return ret;
    }


    @GetMapping(path = "/Dininghall/{id}")
    DiningHall getFoodsById(@PathVariable int id) {
        return diningHallRepository.findById(id);
    }

    @PostMapping(path = "/Dininghall")
    int createFood(@RequestBody DiningHall food) {
        return diningHallRepository.save(food).getId();
    }

    @Transactional
    @DeleteMapping(path = "/Dininghall/{id}")
    String deleteFood(@PathVariable int id) {
        if (diningHallRepository.findById(id) != null) {
            diningHallRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/Dininghall/{id}")
    DiningHall updateFood(@PathVariable int id, @RequestBody DiningHall request) {
        DiningHall foodItem = diningHallRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        diningHallRepository.save(request);
        return diningHallRepository.findById(id);
    }




}
