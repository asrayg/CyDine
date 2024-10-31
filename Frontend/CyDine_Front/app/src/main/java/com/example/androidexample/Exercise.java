// Exercise.java
package com.example.androidexample;

public class Exercise {
    private String name;
    private int timeSpent;
    private int caloriesBurned;

    public Exercise(String name, int timeSpent, int caloriesBurned) {
        this.name = name;
        this.timeSpent = timeSpent;
        this.caloriesBurned = caloriesBurned;
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
}
