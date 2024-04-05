package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage{
  private boolean game;
  public LoadGame(ServerMessageType type, boolean game) {
    super(type);
    this.game = game;
  }

  public boolean isGame() {
    return game;
  }
}
