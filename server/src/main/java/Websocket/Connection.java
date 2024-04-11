package Websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;

public class Connection {
  public String authtoken;
  public Session session;

  public Connection(String authtoken, Session session) {
    this.authtoken= authtoken;
    this.session = session;
  }

  public void send(ServerMessage msg) throws IOException {
    session.getRemote().sendString(new Gson().toJson(msg));
  }
}
