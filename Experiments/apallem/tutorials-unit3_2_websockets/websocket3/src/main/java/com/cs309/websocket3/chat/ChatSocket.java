package com.cs309.websocket3.chat;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{username}") // this is Websocket url
public class ChatSocket {

	private static MessageRepository msgRepo;

	@Autowired
	public void setMessageRepository(MessageRepository repo) {
		msgRepo = repo; // we are setting the static variable
	}

	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException {
		logger.info("Entered into Open");

		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

		sendMessageToPArticularUser(username, getChatHistory());

		String message = "User:" + username + " has Joined the Chat";
		broadcast(message);
	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		logger.info("Entered into Message: Got Message:" + message);
		String username = sessionUsernameMap.get(session);

		if (message.startsWith("@")) {
			String destUsername = message.split(" ")[0].substring(1);
			sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
			sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
		} else if (message.equals("/deleteAll")) { // Command to delete all chat history
			deleteChatHistory();
			broadcast("All chat history has been deleted by " + username);
		} else {
			broadcast(username + ": " + message);
			msgRepo.save(new Message(username, message));
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

		String message = username + " disconnected";
		broadcast(message);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}

	private void sendMessageToPArticularUser(String username, String message) {
		try {
			usernameSessionMap.get(username).getBasicRemote().sendText(message);
		} catch (IOException e) {
			logger.info("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void broadcast(String message) {
		sessionUsernameMap.forEach((session, username) -> {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
				e.printStackTrace();
			}
		});
	}

	// Gets the Chat history from the repository
	private String getChatHistory() {
		List<Message> messages = msgRepo.findAll();

		StringBuilder sb = new StringBuilder();
		if (messages != null && !messages.isEmpty()) {
			for (Message message : messages) {
				sb.append(message.getUserName()).append(": ").append(message.getContent()).append("\n");
			}
		}
		return sb.toString();
	}

	// Method to delete all chat history
	private void deleteChatHistory() {
		msgRepo.deleteAll(); // Deletes all messages from the repository
	}
}