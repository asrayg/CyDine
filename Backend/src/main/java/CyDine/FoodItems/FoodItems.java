package CyDine.FoodItems;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import CyDine.MealPlans.MealPlans;

@Entity
public class FoodItems {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private int protein;
    private int carbs;
    private int fat;
    private int calories;
    private int UserId;
//    @ManyToOne
//    @JsonIgnore
//    private onetoone.Users.User user;

    @ManyToOne
    @JsonIgnore
    private CyDine.MealPlans.MealPlans mealPlans;

    public FoodItems(){
    }

    public FoodItems(String name, int protein, int carbs, int fat, int calories, int id) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.calories = calories;
    }


    public int getFat() {
        return fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getProtein() {
        return protein;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public MealPlans getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(MealPlans mealPlans) {
        this.mealPlans = mealPlans;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
