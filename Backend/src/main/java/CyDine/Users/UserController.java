package CyDine.Users;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import CyDine.tmpObjs.passwordObj;
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
        User user = userRepository.findById(id);
        return user;
    }

    @Transactional
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user) throws ClassNotFoundException, SQLException {
        if (user == null)
            return failure;
        userRepository.save(user);
        Random rand = new Random();
        userRepository.findById(user.getId()).setLogintoken(rand.nextInt(9999999));

//        Class.forName("com.mysql.jdbc.Driver");
//        Connection con = DriverManager.getConnection(
//                "jdbc:mysql://coms-3090-020.class.las.iastate.edu:3306/mydatabase", "root", "123");
//        Statement stmt = con.createStatement();

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
    String deleteUser(@PathVariable int id, @RequestBody passwordObj password){
        if(userRepository.existsById((long)id)) {
            if (userRepository.findById(id).getPassword().equals(password.getPassword())) {
                userRepository.deleteById(id);
                return success;
            }
        }
        return failure;
    }

    @Transactional
    @PostMapping(path ="/users/login/{id}")
    String loginUser(@PathVariable int id, @RequestBody String password){
        User user = userRepository.findById(id);
        if(user.getPassword().equals(password)){
            user.setIfActive(false);
            Random rand = new Random();
            user.setLogintoken(rand.nextInt(999999999));
            System.out.println(user.getLogintoken());
            return success;
        }
        return failure;
    }

    @Transactional
    @PostMapping(path ="/users/logout/{id}")
    String logoutUser(@PathVariable int id, @RequestBody String token){
        if(userRepository.findById(id).getLogintoken() == Integer.parseInt(token) && userRepository.findById(id).getIsActive()){
            userRepository.findById(id).setIfActive(false);
            return success;
        }
        return failure;
    }
}
