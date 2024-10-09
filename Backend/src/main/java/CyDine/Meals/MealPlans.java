package CyDine.Meals;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Entity
public class MealPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<FoodItems> foods;

    private int protein = 0;
    private int carbs = 0;
    private int finalCalories = 0;
    private int fat = 0;


    public MealPlans() {
    }

    public void addFoodItem(FoodItems foodItem) {
        foods.add(foodItem);
        protein += foodItem.getProtein();
        carbs += foodItem.getCarbs();
        fat += foodItem.getFat();
        finalCalories += foodItem.getCalories();
    }

    public int getId() {
        return id;
    }
}
