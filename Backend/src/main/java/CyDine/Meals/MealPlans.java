package CyDine.Meals;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import jakarta.persistence.*;




@Entity
public class MealPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    private List<FoodItems> foods;

    private int protein = 0;
    private int carbs = 0;
    private int finalCalories = 0;
    private int fat = 0;
    LocalDate today;



    public MealPlans() {
        today = LocalDate.now();
    }

    public void addFoodItem(FoodItems foodItem) {
        foods.add(foodItem);
        protein += foodItem.getProtein();
        carbs += foodItem.getCarbs();
        fat += foodItem.getFat();
        finalCalories += foodItem.getCalories();
        System.out.println(foodItem.getCalories());

    }

    public void removeFoodItem(FoodItems foodItem){
        foods.remove(foodItem);
        protein -= foodItem.getProtein();
        carbs -= foodItem.getCarbs();
        fat -= foodItem.getFat();
        finalCalories -= foodItem.getCalories();
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate(){
        return today;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFat() {
        return fat;
    }

    public int getFinalCalories() {
        return finalCalories;
    }

    public int getProtein() {
        return protein;
    }

    public String getFoods() {
        StringBuilder funtime = new StringBuilder();
        for(FoodItems i : foods){
            funtime.append(i.getId()).append(",");
        }
        return funtime.toString();
    }
}
