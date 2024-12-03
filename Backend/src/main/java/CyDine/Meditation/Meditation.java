package CyDine.Meditation;

import CyDine.MealPlanChat.MealPlanMessage;
import CyDine.MealPlans.MealPlans;
import CyDine.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Meditation {

    @ManyToOne
    @JsonIgnore
    private User users;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private LocalDateTime date;
    private int time;
    private int userId;



    public Meditation(){
        this.date = LocalDateTime.now();
    }

    public Meditation(int time, int userId) {
        this.date = LocalDateTime.now();
        this.time = time;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
