package Websocket;

import com.google.gson.Gson;
import model.Message;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


public class WebsocketHandler {
  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

    var conn = connections.getConnection(command.getAuthString());
    if (conn != null) {
      switch (command.getCommandType()) {
        case JOIN_PLAYER -> join(conn, msg);
        case JOIN_OBSERVER -> observe(conn, msg);
        case MAKE_MOVE -> move(conn, msg);
        case LEAVE -> leave(conn, msg);
        case RESIGN -> resign(conn, msg);
      }
    } else {
      Connection.send(session.getRemote(), "unknown user");
    }
  }

  private void join(Connection conn, String msg) throws IOException {
    connections.add(conn.authtoken, conn);
    var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, msg);
    connections.broadcast(conn.authtoken, notification);
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
