package CyDine.Meals;

import CyDine.Meals.FoodItems;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface FoodRepository extends JpaRepository<FoodItems, Long> {

    FoodItems findById(int id);

    void deleteById(int id);

    List<FoodItems> findAll();


}
