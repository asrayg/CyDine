package CyDine.Meals;

import java.util.ArrayList;

public class MealPlans {
    ArrayList<FoodItems> foodItemsList = new ArrayList<>();
    private int protein = 0;
    private int carbs = 0;
    private int finalCalories = 0;
    private int fat = 0;
    private int id = 0;

    public void addFoodItems(FoodItems foodItems) {
        foodItemsList.add(foodItems);
        protein += foodItems.getProtein();
        carbs += foodItems.getCarbs();
        fat += foodItems.getFat();
        finalCalories += foodItems.getCalories();

    }








}
