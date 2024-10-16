package CyDine.Posts.Comments;

import CyDine.MealPlans.MealPlans;
import CyDine.Posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<MealPlans,Integer> {
    Posts findById(int id);
    void deleteById(int id);
//    List<Posts> findAll();

}