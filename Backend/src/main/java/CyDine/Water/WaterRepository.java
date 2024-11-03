package CyDine.Water;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WaterRepository extends JpaRepository<Water, Long> {

    Water findById(int id);

    void deleteById(int id);

    List<Water> findAll();

}

