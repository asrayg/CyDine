package CyDine.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import CyDine.DiningHallMealPlan.DiningHallMealPlan;
import jakarta.persistence.*;


import CyDine.FoodItems.FoodItems;
import CyDine.MealPlans.MealPlans;

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(unique = true)
    private String emailId;
    private Date joiningDate;
    private boolean ifActive;
    private String password;
    private int logintoken;
    private int height;
    private int weight;
    private int age;
    private boolean IsMod;
    private boolean IsAdmin;
    private boolean IsBanned;
    private String dietary_preference;
    private String dietary_restrictions;
    private String fitness_goal;
    private String discordUsername;
    private boolean isWarned;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "mealPlans_id")
    private List<MealPlans> mealPlans;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "diningHallMealPlan_id")
    private List<DiningHallMealPlan> diningHallMealPlan;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "foodItems_id")
    private List<FoodItems> foodItems;




    // =============================== Constructors ================================== //


    public User(String name, String emailId, String password, int height, int weight, String dietary_preference, String dietary_restrictions, String fitness_goal, String discordUsername) {
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.ifActive = true;
        this.height = height;
        this.weight = weight;
        this.dietary_preference = dietary_preference;
        this.dietary_restrictions = dietary_restrictions;
        this.fitness_goal = fitness_goal;
        this.discordUsername = discordUsername;
        mealPlans = new ArrayList<>();
        foodItems = new ArrayList<>();
        isWarned = false;
    }

    public User() {
        isWarned = false;
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
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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


    public String getDietary_preference() {
        return dietary_preference;
    }

    public void setDietary_preference(String dietary_preference) {
        this.dietary_preference = dietary_preference;
    }

    public String getDietary_restrictions() {
        return dietary_restrictions;
    }

    public void setDietary_restrictions(String dietary_restrictions) {
        this.dietary_restrictions = dietary_restrictions;
    }

    public String getFitness_goal() {
        return fitness_goal;
    }

    public void setFitness_goal(String fitness_goal) {
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

    public void addDiningHallMealPlan(DiningHallMealPlan diningHallMealPlan) {
        this.diningHallMealPlan.add(diningHallMealPlan);
    }

    public void removeDiningHallMealPlan(DiningHallMealPlan diningHallMealPlan) {
        this.diningHallMealPlan.remove(diningHallMealPlan);
    }

    public List<DiningHallMealPlan> getDiningHallMealPlan() {
        return diningHallMealPlan;
    }

    public List<FoodItems> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItems> foodItems) {
        this.foodItems = foodItems;
    }

    public String getdiscordUsername() {
        return discordUsername;
    }

    public void setdiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
    }


    public void setWarned(boolean warned) {
        isWarned = warned;
    }

    public boolean isWarned() {
        return isWarned;
    }

    public String getDiscordUsername() {
        return discordUsername;
    }

    public void setDiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
    }

    public void setMealPlans(List<MealPlans> mealPlans) {
        this.mealPlans = mealPlans;
    }

    public void setDiningHallMealPlan(List<DiningHallMealPlan> diningHallMealPlan) {
        this.diningHallMealPlan = diningHallMealPlan;
    }
}
