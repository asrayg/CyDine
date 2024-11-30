package CyDine.SleepTracking;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class SleepEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private double hoursSlept;
    private LocalDate date;

    public SleepEntry() {}

    public SleepEntry(int userId, double hoursSlept, LocalDate date) {
        this.userId = userId;
        this.hoursSlept = hoursSlept;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getHoursSlept() { return hoursSlept; }
    public void setHoursSlept(double hoursSlept) { this.hoursSlept = hoursSlept; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}