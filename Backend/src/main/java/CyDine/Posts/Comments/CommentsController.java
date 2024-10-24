package CyDine.Posts.Comments;


import java.util.List;

import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import CyDine.Users.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CommentsController {

  /*  @Autowired
    private CommentsController commentsController;*/

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
}
