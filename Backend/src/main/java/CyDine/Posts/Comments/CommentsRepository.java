package CyDine.Posts.Comments;

import CyDine.MealPlans.MealPlans;
import CyDine.Posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Integer> {
    Comments findById(int id);
    void deleteById(int id);
//    List<Posts> findAll();

}