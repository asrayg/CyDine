package CyDine.Maps;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodPlaceRepository extends JpaRepository<FoodPlace, Integer> {
    FoodPlace findById(int id);
}
