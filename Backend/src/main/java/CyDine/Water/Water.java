package CyDine.Water;

import CyDine.MealPlans.MealPlans;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Water {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int goal;
    private int total;
    private Date date;
    private int userId;


    public Water(){
    }

    public Water(int goal, int total, int id, int userId) {
        this.id = id;
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
}
