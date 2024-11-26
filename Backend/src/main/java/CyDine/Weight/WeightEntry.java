package CyDine.Weight;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class WeightEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private double weight;
    private LocalDate date;

    public WeightEntry() {
    }

    public WeightEntry(int userId, double weight, LocalDate date) {
        this.userId = userId;
        this.weight = weight;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}