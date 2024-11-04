package CyDine.DiningHall;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DiningHallRepository extends JpaRepository<DiningHall, Long> {

    DiningHall findById(int id);

    void deleteById(int id);

    List<DiningHall> findAll();

}

