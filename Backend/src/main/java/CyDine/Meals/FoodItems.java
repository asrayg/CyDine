package CyDine.Meals;

public class FoodItems {
    private String name;
    private int protein;
    private int carbs;
    private int fat;
    private int calories;
    private int id;

    public FoodItems(String name, int protein, int carbs, int fat, int calories, int id) {
        this.name = name;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public int getProtein() {
        return protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFat() {
        return fat;
    }

    public int getCalories() {
        return calories;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }


}
