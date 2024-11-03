package CyDine.Fitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class FitnessController {

    @Autowired
    FitnessRepository fitnessRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/fitness")
    List<Fitness> getAllFitness() {
        return fitnessRepository.findAll();
    }

    @GetMapping(path = "/users/{userId}/fitness")
    List<Fitness> getAllFitnessForUser(@PathVariable int userId) {
        return fitnessRepository.findAll().stream()
                .filter(f -> f.getUserId() == userId)
                .toList();
    }

    @GetMapping(path = "/fitness/{id}")
    Fitness getFitnessById(@PathVariable int id) {
        return fitnessRepository.findById(id);
    }

    @PostMapping(path = "/fitness")
    Fitness createFitness(@RequestBody Fitness fitness) {
        // Map incoming JSON fields to Fitness object
        fitness.setActivity(fitness.getName());
        fitness.setDuration(fitness.getTime());
        fitness.setCaloriesBurned(fitness.getCalories());
        return fitnessRepository.save(fitness);
    }

    @DeleteMapping(path = "/fitness/{id}")
    String deleteFitness(@PathVariable int id) {
        if (fitnessRepository.findById(id) != null) {
            fitnessRepository.deleteById(id);
            return success;
        }
        return failure;
    }

    @PutMapping("/fitness/{id}")
    Fitness updateFitness(@PathVariable int id, @RequestBody Fitness request) {
        Fitness fitness = fitnessRepository.findById(id);
        if (fitness == null) {
            throw new RuntimeException("Fitness id does not exist");
        }
        request.setId(id);
        return fitnessRepository.save(request);
    }
}