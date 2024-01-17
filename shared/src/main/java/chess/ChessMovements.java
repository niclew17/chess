package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMovement {
  private PieceType type;
  private TeamColor pieceColor;

  public ChessMovement(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    this.type=type;
    this.pieceColor=pieceColor;
  }
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

  }
}
class King extends ChessMovement{

  }
class Queen extends ChessMovement{

}
class Bishop extends ChessMovement{

}
class Knight extends ChessMovement{

}
class Rook extends ChessMovement{

}
class Pawn extends ChessMovement{

}
