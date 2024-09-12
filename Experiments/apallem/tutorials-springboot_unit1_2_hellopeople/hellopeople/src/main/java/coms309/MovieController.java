package coms309;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
public class MovieController {
    // Note that there is only ONE instance of PeopleController in
    // Springboot system.
    HashMap<String, Movies> movieList = new  HashMap<>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the people in the list and returns it in JSON format
    // This controller takes no input.
    // Springboot automatically converts the list to JSON format
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/movie")
    public  HashMap<String,Movies> getAllMovies() {
        return movieList;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/movie")
    public  String createMovie(@RequestBody Movies movie) {
        System.out.println(movie);
        movieList.put(movie.getFirstName(), movie);
        return "New movie "+ movie.getFirstName() + " Saved";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/movie/{firstName}")
    public Movies getMovies(@PathVariable String firstName) {
        Movies m = movieList.get(firstName);
        return m;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method
    @PutMapping("/movie/{firstName}")
    public Movies updatePerson(@PathVariable String firstName, @RequestBody Movies m) {
        movieList.replace(firstName, m);
        return movieList.get(firstName);
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method

    @DeleteMapping("/movie/{firstName}")
    public HashMap<String, Movies> deleteMovies(@PathVariable String firstName) {
        movieList.remove(firstName);
        return movieList;
    }
}

