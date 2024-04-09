package Websocket;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebsocketHandler {
  private final GameDAO gameDAO;
  private final AuthDAO authDAO;

  public WebsocketHandler(GameDAO gameDAO, AuthDAO authDAO) {
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;

  }
  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
    var conn = connections.getConnection(command.,command.getAuthString());
    if (conn != null) {
      switch (command.getCommandType()) {
        case JOIN_PLAYER -> join(conn, msg);
        case JOIN_OBSERVER -> observe(conn, msg);
        case MAKE_MOVE -> move(conn, msg);
        case LEAVE -> leave(conn, msg);
        case RESIGN -> resign(conn, msg);
      }
    } else {
      conn.send("unknown user");
    }
  }

  private void join(Connection conn, String msg) throws IOException {
    JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
    connections.add(command.getGameID(), conn.authtoken, conn);
    String message = String.format("%d User Joined", command.getPlayerColor());
    var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connections.broadcast(command.getGameID(), conn.authtoken, notification);
    var load_game = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, true);
    conn.se
  }
  private void observe(Connection conn, String msg){

  }
  private void move(Connection conn, String msg){

  }
  private void leave(Connection conn, String msg){

  }
  private void resign(Connection conn, String msg){

  }

}
