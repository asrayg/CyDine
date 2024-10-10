package CyDine.MealPlans;


import java.util.List;

import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import CyDine.Users.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MealPlansController {

    @Autowired
    private MealPlansRepository mealPlansRepository;


    @Autowired
    FoodItemsRepository foodItemsRepository;

//    @Autowired
//    User user;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/mealplans")
    List<MealPlans> getAllMealPlans() {
        return mealPlansRepository.findAll();
    }

    @GetMapping(path = "/mealplans/{id}")
    MealPlans getMealPlanById(@PathVariable int id) {
        return mealPlansRepository.findById(id);
    }

    @PostMapping(path = "/mealplans")
    int createMealPlan() {
        MealPlans mealPlan = new MealPlans();
        MealPlans mp = mealPlansRepository.save(mealPlan);
        return mp.getId();
    }

    @PostMapping(path = "/mealplans/additm")
    int createMealPlan2(@RequestBody String mealpln) {
        MealPlans mealPlan = new MealPlans();
        JSONObject job = new JSONObject(mealpln);
        for (String i : job.getString("foodItems").split(",")) {
            mealPlan.addFoodItem(foodItemsRepository.save(new FoodItems(i,0,0,0,0,0)));
        }
        mealPlan.addCarbs(job.getInt("carbs"));
        mealPlan.addProtein(job.getInt("protein"));
        mealPlan.addFat(job.getInt("fat"));
        mealPlan.addCals(job.getInt("finalCalories"));
        MealPlans mp = mealPlansRepository.save(mealPlan);
        return mp.getId();
    }

    @GetMapping(path = "/mealplans/{id}/date")
    String getDate(@PathVariable int id) {
        return mealPlansRepository.findById(id).getDate().toString();
    }

    @PutMapping(path = "/mealplans/{id}/fooditems/add/byName")
    String addFoodItemToMealPlanByName(@PathVariable int id, @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        System.out.println("??????????????????????????????????");
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            for(FoodItems x : foodItemsRepository.findAll()){
                if (x.getName().equalsIgnoreCase(i)){
                    mealPlan.addFoodItem(foodItemsRepository.findById(x.getId()));
                    break;
                }
            }
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }
//TODO: this is bad code remove it, like really, you need to do is in a non idiotic way
    @PutMapping(path = "/mealplans/{id}/fooditems/remove/byName")
    String removeFoodItemFromMealPlanByName(@PathVariable int id, @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            for(FoodItems x : foodItemsRepository.findAll()){
                if (x.getName().equalsIgnoreCase(i)){
                    mealPlan.removeFoodItem(foodItemsRepository.findById(x.getId()));
                    break;
                }
            }
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }


    @PutMapping(path = "/mealplans/{id}/fooditems/add/byId")
    String addFoodItemToMealPlanById(@PathVariable int id, @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            mealPlan.addFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }

    @PutMapping(path = "/mealplans/{id}/fooditems/remove/byId")
    String removeFoodItemFromMealPlanById(@PathVariable int id, @RequestBody String Vaibhav) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null)
            return failure;
        for(String i : Vaibhav.split(",")){
            mealPlan.removeFoodItem(foodItemsRepository.findById(Integer.parseInt(i)));
        }
        mealPlansRepository.save(mealPlan);
        return success;
    }

    @DeleteMapping(path = "/mealplans/{id}")
    String deleteMealPlan(@PathVariable int id) {
        if (mealPlansRepository.existsById(id)) {
//            for (FoodItems fi : mealPlansRepository.findById(id).getFoodItems()) {
//                mealPlansRepository.findById(id).removeFoodItem(fi);
//            }
            mealPlansRepository.deleteById(id);
            return success;
        }
        return failure;
    }


    @PutMapping("/mealplans/{id}/eatfoodagain/{foodid}")
    MealPlans updateMealPlan(@PathVariable int id, @PathVariable int foodid) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealPlan.eatFoodAgain(foodItemsRepository.findById(foodid));
        mealPlansRepository.save(mealPlan);
        return mealPlansRepository.findById(id);
    }

    @PutMapping("/mealplans/{id}")
    MealPlans updateMealPlan(@PathVariable int id, @RequestBody MealPlans request) {
        MealPlans mealPlan = mealPlansRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealPlansRepository.save(request);
        return mealPlansRepository.findById(id);
    }


}
