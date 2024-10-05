package CyDine.Meals;

import CyDine.Meals.MealPlans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository {
    public interface UserRepository extends JpaRepository<MealPlans, Long> {

        MealPlans findById(int id);

        void deleteById(int id);

    }
}
