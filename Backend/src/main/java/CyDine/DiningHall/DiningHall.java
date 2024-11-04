package CyDine.DiningHall;

import CyDine.MealPlans.MealPlans;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class DiningHall {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private int protein;
    private int carbs;
    private int fat;
    private int calories;
    private String dininghall;
    private String time;
    //    @ManyToOne
//    @JsonIgnore
//    private onetoone.Users.User user;

    @ManyToOne
    @JsonIgnore
    private MealPlans mealPlans;

    public DiningHall(){
    }

    public DiningHall(String name, int protein, int carbs, int fat, int calories, int id, String dininghall, String time) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.calories = calories;
        this.dininghall = dininghall;
        this.time = time;
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

    public int getFat() {
        return fat;
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

    public String getDininghall() {
        return dininghall;
    }

    public void setDininghall(String dininghall) {
        this.dininghall = dininghall;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MealPlans getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(MealPlans mealPlans) {
        this.mealPlans = mealPlans;
    }
}
