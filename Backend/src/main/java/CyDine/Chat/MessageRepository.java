package CyDine.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import CyDine.Chat.Message;
public interface MessageRepository extends JpaRepository<Message, Long>{


}

