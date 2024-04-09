package Websocket;

import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String authtoken, Connection conn) {
    connections.put(authtoken, conn);
  }

  public void remove(String authtoken) {
    connections.remove(authtoken);
  }
  public Connection getConnection(String auth){
    return connections.get(auth);
  }
  public void broadcast(String excludeauthtoken, Notification notification) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.authtoken.equals(excludeauthtoken)) {
          c.send(notification.getMessage());
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
