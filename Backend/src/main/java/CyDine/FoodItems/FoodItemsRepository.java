package CyDine.FoodItems;

import CyDine.FoodItems.FoodItems;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface FoodItemsRepository extends JpaRepository<FoodItems, Long> {

    FoodItems findById(int id);

    void deleteById(int id);

    List<FoodItems> findAll();

}

