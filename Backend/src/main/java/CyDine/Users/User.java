package CyDine.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;


import CyDine.FoodItems.FoodItems;
import CyDine.MealPlans.MealPlans;

@Entity
public class User {

    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String emailId;
    private Date joiningDate;
    private boolean ifActive;
    private String password;
    private int logintoken;
    private int Height;
    private int Weight;
    private int age;
    private boolean IsMod;
    private boolean IsAdmin;
    private boolean IsBanned;
    private int dietary_preference;
    private int dietary_restrictions;
    private int fitness_goal;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "mealPlans_id")
    private List<MealPlans> mealPlans;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "foodItems_id")
    private List<FoodItems> foodItems;




     // =============================== Constructors ================================== //


    public User(String name, String emailId, String password) {
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.ifActive = true;
//        TODO: add what ever frount end is senting rn
        mealPlans = new ArrayList<>();
        foodItems = new ArrayList<>();
    }

    public User() {
        mealPlans = new ArrayList<>();
        foodItems = new ArrayList<>();
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public boolean isIfActive() {
        return ifActive;
    }

    public void setIfActive(boolean ifActive) {
        this.ifActive = ifActive;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLogintoken() {
        return logintoken;
    }

    public void setLogintoken(int logintoken) {
        this.logintoken = logintoken;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMod() {
        return IsMod;
    }

    public void setMod(boolean mod) {
        IsMod = mod;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }

    public boolean isBanned() {
        return IsBanned;
    }

    public void setBanned(boolean banned) {
        IsBanned = banned;
    }


    public int getDietary_preference() {
        return dietary_preference;
    }

    public void setDietary_preference(int dietary_preference) {
        this.dietary_preference = dietary_preference;
    }

    public int getDietary_restrictions() {
        return dietary_restrictions;
    }

    public void setDietary_restrictions(int dietary_restrictions) {
        this.dietary_restrictions = dietary_restrictions;
    }

    public int getFitness_goal() {
        return fitness_goal;
    }

    public void setFitness_goal(int fitness_goal) {
        this.fitness_goal = fitness_goal;
    }

    public List<MealPlans> getMealPlans() {
        return mealPlans;
    }

    public void addMealPlans(MealPlans mealPlans) {
        this.mealPlans.add(mealPlans);
    }

    public void removeMealPlans(MealPlans mealPlans) {
        this.mealPlans.remove(mealPlans);
    }

    public List<FoodItems> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItems> foodItems) {
        this.foodItems = foodItems;
    }
}
