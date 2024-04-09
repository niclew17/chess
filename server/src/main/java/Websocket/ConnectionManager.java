package Websocket;

import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();
  public void add(int gameID, String authtoken, Connection conn) {
    connections.get(gameID).put(authtoken, conn);
  }

  public void remove(int gameID, String authtoken) {
    connections.get(gameID).remove(authtoken);
  }
  public Connection getConnection(int gameID, String auth){
    return connections.get(gameID).get(auth);
  }
  public void broadcast(int gameID, String excludeauthtoken, ServerMessage serverMessage) throws IOException, EncodeException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.get(gameID).values()) {
      if (c.session.isOpen()) {
        if (!c.authtoken.equals(excludeauthtoken)) {
          c.send(serverMessage);
        }
      } else {
        removeList.add(c);
      }
    }

    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.authtoken);
    }
  }
}
