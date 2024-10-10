package CyDine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import CyDine.Users.User;
import CyDine.Users.UserRepository;

import java.util.Arrays;


@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


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