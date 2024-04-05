package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {

  private int gameID;

  public int getGameID() {
    return gameID;
  }

  public Leave(String authToken, CommandType commandType, int gameID) {
    super(authToken);
    this.gameID=gameID;
    this.commandType=commandType;
  }

}