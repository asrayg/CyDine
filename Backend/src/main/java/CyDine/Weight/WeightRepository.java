package CyDine.Weight;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WeightRepository extends JpaRepository<WeightEntry, Integer> {
    List<WeightEntry> findByUserIdOrderByDateDesc(int userId);
    List<WeightEntry> findByUserIdOrderByDateAsc(int userId);
    WeightEntry findTopByUserIdOrderByDateDesc(int userId);
}