package CyDine.Users;

import java.util.List;

import CyDine.MealPlans.MealPlans;
import CyDine.MealPlans.MealPlansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MealPlansRepository mealPlansRepository;

// TODO: make it so after mealplan gets make user asignes it to it self


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/users/{userId}/mealplan/{mealPlanId}")
    String assignMealPlan(@PathVariable int userId, @PathVariable int mealPlanId){
        User user = userRepository.findById(userId);
        MealPlans mealPlan = mealPlansRepository.findById(mealPlanId);
        if(user == null || mealPlan == null)
            return failure;
        mealPlan.setUser(user);
        user.addMealPlans(mealPlan);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}/mealplan/{mealPlanId}")
    String deleteMealPlan(@PathVariable int id, @PathVariable int mealPlanId){
        userRepository.findById(id).removeMealPlans(mealPlansRepository.findById(mealPlanId));
        mealPlansRepository.deleteById(mealPlanId);
        return success;
    }


}
