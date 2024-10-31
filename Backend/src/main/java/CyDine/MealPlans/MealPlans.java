package CyDine.MealPlans;


import CyDine.Users.User;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import CyDine.FoodItems.FoodItems;

import java.time.LocalDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import CyDine.FoodItems.FoodItems;
import CyDine.MealPlans.MealPlans;

@Entity
public class MealPlans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int protein = 0;
    private int carbs = 0;
    private int finalCalories = 0;
    private int fat = 0;
    LocalDate date;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "foodItems_id")
    private List<FoodItems> foodItems;

    @ManyToOne
    @JsonIgnore
    private CyDine.Users.User user;

    public MealPlans() {
        date = LocalDate.now();
        foodItems = new ArrayList<>();
    }

    public void addFoodItem(FoodItems foodItem) {
        foodItems.add(foodItem);
        protein += foodItem.getProtein();
        carbs += foodItem.getCarbs();
        fat += foodItem.getFat();
        finalCalories += foodItem.getCalories();
        System.out.println(foodItem.getCalories());
    }

    public void removeFoodItem(FoodItems foodItem){
        protein -= foodItem.getProtein();
        carbs -= foodItem.getCarbs();
        fat -= foodItem.getFat();
        finalCalories -= foodItem.getCalories();
        foodItems.remove(foodItem);
        System.out.println("msodfhlksdjf   : ");
//        foodItems.remove(tmp+1);

    }

    public void eatFoodAgain(FoodItems foodItem) {
        foodItems.add(foodItem);
        protein += foodItem.getProtein();
        carbs += foodItem.getCarbs();
        fat += foodItem.getFat();
        finalCalories += foodItem.getCalories();
        System.out.println(foodItem.getCalories());
    }

    public List<FoodItems> getFoodItems() {
        return foodItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFinalCalories() {
        return finalCalories;
    }

    public void setFinalCalories(int finalCalories) {
        this.finalCalories = finalCalories;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void addCarbs(int i){
        carbs+=i;
    }

    public void addCals(int i){
        finalCalories+=i;
    }

    public void addFat(int i){
        fat+=i;
    }

    public void addProtein(int i){
        protein+=i;
    }

}
