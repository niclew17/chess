package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGame extends ServerMessage{
  private ChessBoard board;
  private String color;
  private ChessGame game;


  public LoadGame(ServerMessageType type, ChessBoard board, String color, ChessGame game) {
    super(type);
    this.board = board;
    this.color =color;
    this.game = game;
  }

  public ChessGame getGame() {
    return game;
  }

  public ChessBoard getBoard() {
    return board;
  }

  public String getColor() {
    return color;
  }
}
