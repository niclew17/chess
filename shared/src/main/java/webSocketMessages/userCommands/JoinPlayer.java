package webSocketMessages.userCommands;

public class JoinPlayer extends UserGameCommand{
  private String playerColor;
  public JoinPlayer(String authToken, int gameID, String playerColor) {
    super(authToken, gameID);

    this.playerColor = playerColor;
    this.commandType = CommandType.JOIN_PLAYER;
  }

  public String getPlayerColor() {
    return playerColor;
  }

}
