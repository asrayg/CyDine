package CyDine.Fitness;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FitnessRepository extends JpaRepository<Fitness, Integer> {

    Fitness findById(int id);

    void deleteById(int id);

    List<Fitness> findAll();
}