package Websocket;

import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
  void notify(ServerMessage notification, String messages);
}
