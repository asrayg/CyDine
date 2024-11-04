package CyDine.DiningHallMealPlan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiningHallMealPlanRepository extends JpaRepository<DiningHallMealPlan,Integer> {
    DiningHallMealPlan findById(int id);
    void deleteById(int id);
    List<DiningHallMealPlan> findAll();

}
