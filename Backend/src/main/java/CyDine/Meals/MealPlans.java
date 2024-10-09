package CyDine.Meals;

import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MealPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private ArrayList<FoodItems> foodItemsList = new ArrayList<>();
    private int protein = 0;
    private int carbs = 0;
    private int finalCalories = 0;
    private int fat = 0;

    // No-argument constructor for JPA
    public MealPlans() {
    }

    // Constructor with parameters
    public MealPlans(ArrayList<FoodItems> foodItems) {
        this.foodItemsList.addAll(foodItems);

    }

    public void addFoodItems(FoodItems foodItems) {
        foodItemsList.add(foodItems);
        protein += foodItems.getProtein();
        carbs += foodItems.getCarbs();
        fat += foodItems.getFat();
        finalCalories += foodItems.getCalories();
    }
}
