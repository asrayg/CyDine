package CyDine;

import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import CyDine.Scraper.Scraper;
import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import CyDine.Users.User;
import CyDine.Users.UserRepository;


import java.util.Arrays;
import java.util.HashMap;


@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//        SpringApplication.run(Websocket3Application.class, args);
//        HashMap<String, JSONArray> tmp = new Scraper().getPlaces();
//        System.out.println(tmp.get("seasons-marketplace-2-2").);
    }


    @Bean
    CommandLineRunner initUser(UserRepository userRepository, FoodItemsRepository foodItemsRepository) {
        return args -> {
              User user1 = new User("John", "john@somemail.com", "Hey", 4, 5, "Bulking", "Peanut", "Hype");
//            User user2 = new User("Jane", "jane@somemail.com", "Hey");
//            User user3 = new User("Justin", "justin@somemail.com", "Hey");
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
            FoodItems food1 = new FoodItems("Chicken", 1, 2, 3, 4, 5);
            FoodItems food2 = new FoodItems("Peas", 1, 2, 3, 4, 6);
            FoodItems food3 = new FoodItems("Taco", 1, 2, 3, 4, 7);
            foodItemsRepository.save(food1);
            foodItemsRepository.save(food2);
            foodItemsRepository.save(food3);

        };
    }



}
