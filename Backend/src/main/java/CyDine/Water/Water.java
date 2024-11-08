package CyDine.Water;

import CyDine.MealPlanChat.MealPlanMessage;
import CyDine.MealPlans.MealPlans;
import CyDine.Scraper.Scraper;
import CyDine.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

import java.util.Date;

@Entity
public class Water {

    @ManyToOne
    @JsonIgnore
    private User users;

    @ManyToMany
    @JsonIgnore
    private List<MealPlanMessage> mealPlanMessages;

    @OneToMany
    @JsonIgnore
    private List<MealPlans> mealPlans;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private int goal;
    private int total;
    private Date date;
    private int userId;


    public Water(){
        this.date = new Date();
    }

    public Water(int goal, int total, int userId) {
        this.date = new Date();
        this.goal = goal;
        this.total = total;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getTotal() {
        return total;
    }

    public int addToTotal(int t) {
        return total+=t;
    }

    public int addToGoal(int t) {
        return goal+=t;
    }


    public void setTotal(int total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return users;
    }

    public void setUser(User user) {
        this.users = user;
    }
//
    public List<MealPlanMessage> getMealPlanMessages() {
        return mealPlanMessages;
    }

    public void setMealPlanMessages(List<MealPlanMessage> mealPlanMessages) {
        this.mealPlanMessages = mealPlanMessages;
    }

    public List<MealPlans> getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(List<MealPlans> mealPlans) {
        this.mealPlans = mealPlans;
    }
}
