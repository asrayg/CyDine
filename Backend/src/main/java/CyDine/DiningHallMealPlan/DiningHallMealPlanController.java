package CyDine.DiningHallMealPlan;


import CyDine.DiningHall.DiningHall;
import CyDine.DiningHall.DiningHallRepository;
import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@RestController
public class DiningHallMealPlanController {

    @Autowired
    private DiningHallMealPlanRepository diningHallMealPlanRepository;


    @Autowired
    DiningHallRepository foodItemsRepository;

//    @Autowired
//    User user;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/DHmealplans")
    List<DiningHallMealPlan> getAllMealPlans() {
        return diningHallMealPlanRepository.findAll();
    }

    @GetMapping(path = "/DHmealplans/{id}")
    DiningHallMealPlan getMealPlanById(@PathVariable int id) {
        return diningHallMealPlanRepository.findById(id);
    }

    @PostMapping(path = "/DHmealplans")
    int createMealPlan() {
        DiningHallMealPlan mealPlan = new DiningHallMealPlan();
        DiningHallMealPlan mp = diningHallMealPlanRepository.save(mealPlan);
        return mp.getId();
    }

    @GetMapping(path = "/DHmealplans/{id}/date")
    String getDate(@PathVariable int id) {
        return diningHallMealPlanRepository.findById(id).getDate().toString();
    }

    @PutMapping(path = "/DHmealplans/{id}/fooditems/add/byName")
    String addFoodItemToMealPlanByName(@PathVariable int id, @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        System.out.println("??????????????????????????????????");
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            System.out.println(i);
            for(DiningHall x : foodItemsRepository.findAll()){
                if (x.getName().equalsIgnoreCase(i) && x.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.now())){
                    System.out.println("SLDKF");
                        System.out.println("1");
                        mealPlan.addFoodItem(foodItemsRepository.findById(x.getId()));
                        break;

                }
            }
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }

    @PutMapping(path = "/DHmealplans/{id}/fooditems/remove/byName")
    String removeFoodItemFromMealPlanByName(@PathVariable int id, @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            for(DiningHall x : foodItemsRepository.findAll()){
                if (x.getName().equalsIgnoreCase(i)){
                    mealPlan.removeFoodItem(foodItemsRepository.findById(x.getId()));
                    break;
                }
            }
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }


    @PutMapping(path = "/DHmealplans/{id}/fooditems/add/byId")
    String addFoodItemToMealPlanById(@PathVariable int id, @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){

            mealPlan.addFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }

    @PutMapping(path = "/DHmealplans/{id}/fooditems/remove/byId")
    String removeFoodItemFromMealPlanById(@PathVariable int id, @RequestBody String Vaibhav) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            mealPlan.removeFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        diningHallMealPlanRepository.save(mealPlan);
        return success;
    }

    @DeleteMapping(path = "/DHmealplans/{id}")
    String deleteMealPlan(@PathVariable int id) {
        if (diningHallMealPlanRepository.existsById(id)) {
            System.out.println("????");
            for (DiningHall fi : diningHallMealPlanRepository.findById(id).getFoodItems()) {
                System.out.println(fi.getName());
                diningHallMealPlanRepository.findById(id).removeFoodItem(fi);
            }
            diningHallMealPlanRepository.deleteById(id);
            return success;
        }
        return failure;
    }


    @PutMapping("/DHmealplans/{id}/eatfoodagain/{foodid}")
    DiningHallMealPlan updateMealPlan(@PathVariable int id, @PathVariable int foodid) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealPlan.eatFoodAgain(foodItemsRepository.findById(foodid));
        diningHallMealPlanRepository.save(mealPlan);
        return diningHallMealPlanRepository.findById(id);
    }

    @PutMapping("/DHmealplans/{id}")
    DiningHallMealPlan updateMealPlan(@PathVariable int id, @RequestBody DiningHallMealPlan request) {
        DiningHallMealPlan mealPlan = diningHallMealPlanRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        diningHallMealPlanRepository.save(request);
        return diningHallMealPlanRepository.findById(id);
    }


}
