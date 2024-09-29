package CyDine.Users;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }


    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);

        if(user == null) {
            throw new RuntimeException("user id does not exist");
        }
        else if (user.getId() != id){
            throw new RuntimeException("path variable id does not match User request id");
        }

        userRepository.save(request);
        return userRepository.findById(id);
    }
    @Transactional
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id, @RequestBody String password){
        System.out.println(userRepository.findById(id).getPassword());
        System.out.println("------------------------------------------------------");
        if(userRepository.findById(id).getPassword().equals(password)){
            userRepository.deleteById(id);
            return success;
        }
        return failure;
    }
}
