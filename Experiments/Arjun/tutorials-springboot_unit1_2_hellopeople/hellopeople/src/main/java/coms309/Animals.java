package coms309;

public class Animals {

    private String name;

    private String species;

    private String location;

    private String climate;

    private Integer age;

    public Animals(String name, String species, String location, String climate, Integer age){
        this.name = name;
        this.species = species;
        this.location = location;
        this.climate = climate;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void addAge() {
        this.age++;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Animals{" +
                "name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", location='" + location + '\'' +
                ", climate='" + climate + '\'' +
                ", age=" + age +
                '}';
    }


}
