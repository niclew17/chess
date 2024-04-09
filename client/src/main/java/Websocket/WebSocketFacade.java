package Websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DataAccessException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String messages) {
          try {
            ServerMessage message = new Gson().fromJson(messages, ServerMessage.class);
            notificationHandler.notify(message);
          } catch(Exception ex) {
            notificationHandler.notify(new ServerMessage(ServerMessage.ServerMessageType.ERROR));
          }

        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new DataAccessException(ex.getMessage());
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }

  public void joinPlayer(String authToken, int gameID, String color) throws IOException {
    try {
      var joinPlayer = new JoinPlayer(authToken, UserGameCommand.CommandType.JOIN_PLAYER, gameID, color);
      this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
    } catch (IOException ex) {
      throw new IOException(ex.getMessage());
    }
  }






}
