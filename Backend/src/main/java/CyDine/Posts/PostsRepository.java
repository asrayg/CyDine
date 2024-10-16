package CyDine.Posts;

import CyDine.MealPlans.MealPlans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<MealPlans,Integer>{
    Posts findById(int id);
    void deleteById(int id);
//    List<Posts> findAll();
}