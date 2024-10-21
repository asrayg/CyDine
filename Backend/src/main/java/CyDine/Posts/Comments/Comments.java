package CyDine.Posts.Comments;


import CyDine.Users.User;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import CyDine.FoodItems.FoodItems;

import java.time.LocalDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    private CyDine.Users.User user;


    private String comment;




    public Comments(String comment){
        this.comment=comment;
    }


    public Comments(){
    }
}