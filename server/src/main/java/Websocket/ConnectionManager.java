package Websocket;

import webSocketMessages.serverMessages.ServerMessage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();
  public final ConcurrentHashMap<Integer, Boolean> resignedboards = new ConcurrentHashMap<>();
  public void addPlayer(int gameID, String authtoken, Connection conn){
    connections.get(gameID).put(authtoken, conn);
  }

  public void cleanConnections(){
    connections.clear();
    resignedboards.clear();
  }
  public void clearPlayer(int gameID, String authtoken){
    connections.get(gameID).remove(authtoken);
  }
  public boolean getResignedBoard(int gameID){
    return resignedboards.get(gameID);
  }
  public void setResigned(int gameID){
    resignedboards.put(gameID, true);
  }

  public void addGame(int gameID) {
    connections.put(gameID, new ConcurrentHashMap<>());
    resignedboards.put(gameID, false);
  }

  public ConcurrentHashMap<String, Connection> getGame(int gameID){
    if(connections == null){
      return null;
    }
    else {
      return connections.get(gameID);
    }
  }

  public Connection getConnection(int gameID, String auth){
    if(connections.get(gameID)== null){
      return null;
    }
    else {
      return connections.get(gameID).get(auth);
    }
  }
  public void broadcast(int gameID, String excludeauthtoken, ServerMessage serverMessage) throws IOException {
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
