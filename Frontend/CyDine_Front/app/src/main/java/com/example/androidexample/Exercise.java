package com.example.androidexample;

/**
 * class for excerise
 */

public class Exercise {
    private int id;
    private String name;
    private int timeSpent;
    private int caloriesBurned;

    public Exercise(int id, String name, int timeSpent, int caloriesBurned) {
        this.id = id;
        this.name = name;
        this.timeSpent = timeSpent;
        this.caloriesBurned = caloriesBurned;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
}
