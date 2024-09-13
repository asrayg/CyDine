package coms309;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
public class MovieController {

    HashMap<String, Movies> movieList = new  HashMap<>();

    @GetMapping("/movie")
    public  HashMap<String,Movies> getAllMovies() {
        return movieList;
    }

    @PostMapping("/movie")
    public  String createMovie(@RequestBody Movies movie) {
        System.out.println(movie);
        movieList.put(movie.getFirstName(), movie);
        return "New movie "+ movie.getFirstName() + " Saved";
    }


    @GetMapping("/movie/{firstName}")
    public Movies getMovies(@PathVariable String firstName) {
        Movies m = movieList.get(firstName);
        return m;
    }

    @PutMapping("/movie/{firstName}")
    public Movies updateMovie(@PathVariable String firstName, @RequestBody Movies m) {
        movieList.replace(firstName, m);
        return movieList.get(firstName);
    }

    @DeleteMapping("/movie/{firstName}")
    public HashMap<String, Movies> deleteMovies(@PathVariable String firstName) {
        movieList.remove(firstName);
        return movieList;
    }
}

