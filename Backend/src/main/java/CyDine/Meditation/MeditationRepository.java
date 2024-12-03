package CyDine.Meditation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MeditationRepository extends JpaRepository<Meditation, Long> {

    Meditation findById(int id);

    void deleteById(int id);

    List<Meditation> findAll();


}

