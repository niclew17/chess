package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
  private ChessMove move;
  private String color;




  public MakeMove(String authToken, int gameID, ChessMove move, String color) {
    super(authToken, gameID);
    this.move = move;
    this.commandType=CommandType.MAKE_MOVE;
    this.color = color;
  }

  public String getColor() {
    return color;
  }

  public ChessMove getMove() {
    return move;
  }

}
