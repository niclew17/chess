package webSocketMessages.userCommands;

public class JoinPlayer extends UserGameCommand{
  private int gameID;
  private String playerColor;
  public JoinPlayer(String authToken, CommandType commandType, int gameID, String playerColor) {
    super(authToken);
    this.gameID = gameID;
    this.playerColor = playerColor;
    this.commandType = commandType;
  }

  public int getGameID() {
    return gameID;
  }

  public String getPlayerColor() {
    return playerColor;
  }

}
