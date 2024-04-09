package Websocket;

import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

public class Connection {
  public String authtoken;
  public Session session;

  public Connection(String authtoken, Session session) {
    this.authtoken= authtoken;
    this.session = session;
  }

  public void send(ServerMessage msg) throws IOException, EncodeException {

    session.getBasicRemote().sendObject(msg);
  }
}
