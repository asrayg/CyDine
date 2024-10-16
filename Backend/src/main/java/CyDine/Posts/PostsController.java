package CyDine.Posts;


import java.util.List;

import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import CyDine.Users.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class PostsController {

    @Autowired
    private PostsController postsController;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
}
