package CyDine.Meals;



public class MealController {

    @Autowired
    MealRepository mealRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/mealplans")
    List<MealPlans> getAllMealPlans() {
        return mealRepository.findAll();
    }

    @GetMapping(path = "/mealplans/{id}")
    MealPlans getMealPlanById(@PathVariable int id) {
        return mealRepository.findById(id);
    }

    @PostMapping(path = "/mealplans")
    String createMealPlan(@RequestBody MealPlans mealPlan) {
        if (mealPlan == null)
            return failure;
        mealRepository.save(mealPlan);
        return success;
    }

    @PostMapping(path = "/mealplans/{id}/fooditems")
    String addFoodItemToMealPlan(@PathVariable int id, @RequestBody FoodItems foodItem) {
        MealPlans mealPlan = mealRepository.findById(id);
        if (mealPlan == null)
            return failure;
        mealPlan.addFoodItems(foodItem);
        mealRepository.save(mealPlan);
        return success;
    }

    @DeleteMapping(path = "/mealplans/{id}")
    String deleteMealPlan(@PathVariable int id) {
        if (mealRepository.findById(id) != null) {
            mealRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/mealplans/{id}")
    MealPlans updateMealPlan(@PathVariable int id, @RequestBody MealPlans request) {
        MealPlans mealPlan = mealRepository.findById(id);
        if (mealPlan == null) {
            throw new RuntimeException("Meal plan id does not exist");
        }
        mealRepository.save(request);
        return mealRepository.findById(id);
    }



}
