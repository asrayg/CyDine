package CyDine.Fitness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Fitness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int time;
    private String name;
    private int calories;
    private Date date;
    private int userId;
    private int currentStreak;

    public Fitness() {
        this.date = new Date();
    }

    public Fitness(int calories, String name, int time, int userId) {
        this.date = new Date();
        this.calories = calories;
        this.name = name;
        this.time = time;
        this.userId = userId;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Date getDate() {
        return date;
    }

    public void setActivity(String activity) {
        this.name = activity;
    }

    public void setDuration(int duration) {
        this.time = duration;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.calories = caloriesBurned;
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
}