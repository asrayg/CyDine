package CyDine.FoodItems;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodItemsController{

    @Autowired
    FoodItemsRepository foodItemsRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/FoodItem")
    List<FoodItems> getAllFoods() {
        return foodItemsRepository.findAll();
    }

    @GetMapping(path = "/FoodItem/{id}")
    FoodItems getFoodsById(@PathVariable int id) {
        return foodItemsRepository.findById(id);
    }

    @PostMapping(path = "/FoodItem")
    int createFood(@RequestBody FoodItems food) {
        return foodItemsRepository.save(food).getId();
    }

    @Transactional
    @DeleteMapping(path = "/FoodItem/{id}")
    String deleteFood(@PathVariable int id) {
        if (foodItemsRepository.findById(id) != null) {
            foodItemsRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/FoodItem/{id}")
    FoodItems updateFood(@PathVariable int id, @RequestBody FoodItems request) {
        FoodItems foodItem = foodItemsRepository.findById(id);
        if (foodItem == null) {
            throw new RuntimeException("food id does not exist");
        }
        foodItemsRepository.save(request);
        return foodItemsRepository.findById(id);
    }

    @GetMapping(path = "/users/{id}/FoodItems")
    List<FoodItems> getUserFoodsById(@PathVariable int id){
        List<FoodItems> tmp = new ArrayList<>();
        for (FoodItems t: foodItemsRepository.findAll()){
            if (t.getUserId() == id){
                tmp.add(t);
            }
        }
        return tmp;
    }

}
