package Websocket;

import javax.websocket.Session;
import java.io.IOException;

public class Connection {
  public String authtoken;
  public Session session;

  public Connection(String authtoken, Session session) {
    this.authtoken= authtoken;
    this.session = session;
  }

  public void send(String msg) throws IOException {
    session.getBasicRemote().sendText(msg);
  }
}
