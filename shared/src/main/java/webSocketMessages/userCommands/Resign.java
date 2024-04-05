package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {

  private int gameID;

  public Resign(String authToken, CommandType commandType, int gameID) {
    super(authToken);
    this.gameID=gameID;
    this.commandType=commandType;
  }
  public int getGameID() {
    return gameID;
  }
}