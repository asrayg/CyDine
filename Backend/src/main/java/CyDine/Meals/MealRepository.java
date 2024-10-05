package CyDine.Meals;

import org.springframework.data.jpa.repository.JpaRepository;

public class MealRepository {
    public interface MealRepository extends JpaRepository<MealPlans, Long> {

        MealPlans findById(int id);

        void deleteById(int id);

    }
}
