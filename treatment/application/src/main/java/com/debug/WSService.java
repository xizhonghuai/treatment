/**
 * 
 */
package com.debug;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XIZHONGHUAI
 *
 */
@ServerEndpoint("/ws")
@Component
public class WSService {
	private static List<Session> websocketSession = new ArrayList<Session>();
	private Session session;
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			//session.getBasicRemote().sendText("error:" + e.toString());
		}

	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		websocketSession.add(session);
		//todo 权限
	}
	@OnClose
	public void onClose() {
		websocketSession.remove(session);
	}

	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}

	public static void sendMessage(String message)  {

		for (int i = 0; i < websocketSession.size(); i++) {
			try {
				websocketSession.get(i).getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
