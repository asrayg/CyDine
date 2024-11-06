package CyDine.MealPlanChat;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@ServerEndpoint(value = "/mpchat/{userId}")
public class MealPlanChatSocket {

  // cannot autowire static directly (instead we do it by the below
  // method
	private static MealPlanMessageRepository msgRepo;

	/*
   * Grabs the MessageRepository singleton from the Spring Application
   * Context.  This works because of the @Controller annotation on this
   * class and because the variable is declared as static.
   * There are other ways to set this. However, this approach is
   * easiest.
	 */
	@Autowired
	public void setMealPlanMessageRepository(MealPlanMessageRepository repo) {
		msgRepo = repo;  // we are setting the static variable
	}

	// Store all socket session and their corresponding username.
	private static Map<Session, Integer> sessionUserIdMap = new Hashtable<>();
	private static Map<Integer, Session> userIdSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(MealPlanChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("userId") int userId)
      throws IOException {
        System.out.println("OPEN MEALPLAN");
        sessionUserIdMap.put(session, userId);
        userIdSessionMap.put(userId, session);

        List<MealPlanMessage> messages = msgRepo.findAll();
        for(MealPlanMessage m : messages){
            sendMessageToPArticularUser(userId, m.toJson());
        }
	}


	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
        JSONObject tmp =new JSONObject(message);
        MealPlanMessage mpMessage = new MealPlanMessage(tmp.getInt("userId"),tmp.getString("message"),tmp.getInt("mealplanId"));
        broadcast(mpMessage.toJson());
		msgRepo.save(mpMessage);
	}


	@OnClose
	public void onClose(Session session) throws IOException {
		int username = sessionUserIdMap.get(session);
        sessionUserIdMap.remove(session);
        userIdSessionMap.remove(username);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


	private void sendMessageToPArticularUser(int userId, String message) {
		try {
            userIdSessionMap.get(userId).getBasicRemote().sendText(message);
		}
    catch (IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}


	private void broadcast(String message) {
        sessionUserIdMap.forEach((session, username) -> {
			try {
				session.getBasicRemote().sendText(message);
			} 
      catch (IOException e) {
				logger.info("Exception: " + e.getMessage().toString());
				e.printStackTrace();
			}

		});

	}


}
