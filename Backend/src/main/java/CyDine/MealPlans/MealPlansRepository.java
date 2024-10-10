package CyDine.MealPlans;

import CyDine.MealPlans.MealPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MealPlansRepository extends JpaRepository<MealPlans,Integer> {
    MealPlans findById(int id);
    void deleteById(int id);
    List<MealPlans> findAll();

}
