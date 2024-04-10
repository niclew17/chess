package webSocketMessages.serverMessages;

import chess.ChessBoard;

public class LoadGame extends ServerMessage{
  private ChessBoard game;
  private String color;
  public LoadGame(ServerMessageType type, ChessBoard game, String color) {
    super(type);
    this.game = game;
    this.color =color;
  }

  public ChessBoard getGame() {
    return game;
  }

  public String getColor() {
    return color;
  }
}
