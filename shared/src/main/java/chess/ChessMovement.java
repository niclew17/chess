package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public abstract class ChessMovement {
  private ChessPiece.PieceType type;
  private ChessGame.TeamColor pieceColor;
  private ChessBoard board;
  private ChessPosition position;


  public ChessMovement(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition position) {
    this.pieceColor=pieceColor;
    this.type=type;
    this.board = board;
    this.position = position;
  }

  public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor);
  public boolean outOfBounds(ChessPosition position) {
    if (position.getRow() > 8 || position.getColumn() > 8) {
      return true;
    }
    else{
      return false;
    }
  }
  public boolean isOpposingTeam(ChessPosition myposition, ChessPosition newposition, ChessBoard board){
    if(board.getPiece(myposition).getTeamColor().equals(board.getPiece(newposition).getTeamColor())){
      return false;
    }
    else{
      return true;
    }
  }

}
class King extends ChessMovement{

  public King(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition position) {
    super(pieceColor, type, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    return null;
  }
}
class Queen extends ChessMovement{

  public Queen(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition position) {
    super(pieceColor, type, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    return null;
  }
}
class Bishop extends ChessMovement{

  public Bishop(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition position) {
    super(pieceColor, type, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> Collection = new ArrayList<>();
    for(int i = 1; i < 8; i++){
      if(board.getPiece(myPosition.getRow() +i, myPosition.getColumn() + i))
    }




    return Collection;
  }
}
class Knight extends ChessMovement{

  public Knight(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition position) {
    super(pieceColor, type, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    return null;
  }
}
class Rook extends ChessMovement{

  public Rook(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition position) {
    super(pieceColor, type, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    return null;
  }
}
class Pawn extends ChessMovement{

  public Pawn(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition position) {
    super(pieceColor, type, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    return null;
  }
}
