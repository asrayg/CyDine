package CyDine.DiningHallMealPlan;


import CyDine.DiningHall.DiningHall;
import CyDine.FoodItems.FoodItems;
import CyDine.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DiningHallMealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private int protein = 0;
    private int carbs = 0;
    private int finalCalories = 0;
    private int fat = 0;
    LocalDate date;
    private String userEmail;


    @ManyToMany(cascade = CascadeType.ALL)
    private List<DiningHall> foodItems;

    @ManyToOne
    @JsonIgnore
    private User user;

//    @OneToMany
//    @JsonIgnore
//    private CyDine.Posts.Posts post;

    public DiningHallMealPlan(User user) {
        date = LocalDate.now();
        foodItems = new ArrayList<>();
        this.userEmail = user.getEmailId();
    }

    public DiningHallMealPlan(int userId) {
        date = LocalDate.now();
        foodItems = new ArrayList<>();
        this.userEmail = user.getEmailId();
    }

    public DiningHallMealPlan() {

    }

    public void addFoodItem(DiningHall foodItem) {
        foodItems.add(foodItem);
        protein += foodItem.getProtein();
        carbs += foodItem.getCarbs();
        fat += foodItem.getFat();
        finalCalories += foodItem.getCalories();
        System.out.println(foodItem.getCalories());
    }

    public void removeFoodItem(DiningHall foodItem){
        protein -= foodItem.getProtein();
        carbs -= foodItem.getCarbs();
        fat -= foodItem.getFat();
        finalCalories -= foodItem.getCalories();
//        foodItems.remove(foodItem);
        System.out.println("msodfhlksdjf   : ");
//        foodItems.remove(tmp+1);

    }

    public void eatFoodAgain(DiningHall foodItem) {
        foodItems.add(foodItem);
        protein += foodItem.getProtein();
        carbs += foodItem.getCarbs();
        fat += foodItem.getFat();
        finalCalories += foodItem.getCalories();
        System.out.println(foodItem.getCalories());
    }

    public List<DiningHall> getFoodItems() {
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
        if (user != null) {
            this.userEmail = user.getEmailId();
        }
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
