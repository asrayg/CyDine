package CyDine.Meals;

import CyDine.Meals.MealPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MealRepository extends JpaRepository<MealPlans, Long> {

    MealPlans findById(int id);

    void deleteById(int id);

    List<MealPlans> findAll();

}

