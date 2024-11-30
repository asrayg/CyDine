package CyDine.SleepTracking;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SleepRepository extends JpaRepository<SleepEntry, Integer> {
    List<SleepEntry> findByUserIdOrderByDateDesc(int userId);
    SleepEntry findTopByUserIdOrderByDateDesc(int userId);
}
