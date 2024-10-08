package CyDine.Meals;

import java.util.ArrayList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Entity
public class MealPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    ArrayList<FoodItems> foodItemsList = new ArrayList<>();
    private int protein = 0;
    private int carbs = 0;
    private int finalCalories = 0;
    private int fat = 0;

    public void addFoodItems(FoodItems foodItems) {
        foodItemsList.add(foodItems);
        protein += foodItems.getProtein();
        carbs += foodItems.getCarbs();
        fat += foodItems.getFat();
        finalCalories += foodItems.getCalories();

    }

}
