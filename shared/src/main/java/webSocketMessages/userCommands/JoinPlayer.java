package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
  private int gameID;
  private ChessGame.TeamColor playerColor;
  public JoinPlayer(String authToken, CommandType commandType, int gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.gameID = gameID;
    this.playerColor = playerColor;
    this.commandType = commandType;
  }

  public int getGameID() {
    return gameID;
  }

  public ChessGame.TeamColor getPlayerColor() {
    return playerColor;
  }

}
