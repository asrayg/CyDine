package CyDine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import CyDine.Users.User;
import CyDine.Users.UserRepository;
import CyDine.Scraper.Scrape;

import java.util.Arrays;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        Scrape t = new Scrape();
        System.out.println(Arrays.toString(t.getPlaces()));
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {
            User user1 = new User("John", "john@somemail.com", "Hey");
            User user2 = new User("Jane", "jane@somemail.com", "Hey");
            User user3 = new User("Justin", "justin@somemail.com", "Hey");
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

        };
    }

}
