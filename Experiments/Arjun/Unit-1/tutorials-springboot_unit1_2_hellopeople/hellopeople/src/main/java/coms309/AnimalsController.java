package coms309;

import coms309.people.Person;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class AnimalsController {

   HashMap<String, Animals> animalList = new HashMap<>();

    @GetMapping("/animals")
    public HashMap<String, Animals> getAllAnimals() {
        return animalList;
    }

    @PostMapping("/animals")
    public String createAnimal(@RequestBody Animals animal) {
        System.out.println(animal);
        animalList.put(animal.getName(), animal);
        return "New animal " + animal.getName() + " saved";
    }

    @GetMapping("/animals/{name}")
    public Animals getAnimal(@PathVariable String name) {
        return animalList.get(name);
    }

    @PutMapping("/animals/{name}")
    public Animals changeLocation(@PathVariable String name, @RequestBody String newloc) {
        Animals animal = animalList.get(name);
        if (animal != null) {
            animal.setLocation(newloc);
        }
        return animal;
    }

    @PutMapping("/animals/{name}/birthday")
    public Animals birthday(@PathVariable String name) {
        animalList.get(name).addAge();;
        return animalList.get(name);
    }

    @DeleteMapping("/animals/{name}")
    public HashMap<String, Animals> deleteAnimal(@PathVariable String name) {
        animalList.remove(name);
        return animalList;
    }

    @GetMapping("/animals/avrageage")
    public double getAvrageAge() {
        if (animalList.isEmpty()) {
            return 0.0;
        }
        double totalAge = 0;
        for (Animals animal : animalList.values()) {
            totalAge += animal.getAge();
        }
        return totalAge / animalList.size();
    }

}

