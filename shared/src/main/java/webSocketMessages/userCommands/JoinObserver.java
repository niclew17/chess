package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
  private int gameID;

  public JoinObserver(String authToken, CommandType commandType, int gameID) {
    super(authToken);
    this.gameID = gameID;
    this.commandType = commandType;
  }

  public int getGameID() {
    return gameID;
  }

}
