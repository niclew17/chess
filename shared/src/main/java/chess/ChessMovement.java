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


  public ChessMovement(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    this.pieceColor=pieceColor;
    this.board = board;
    this.position = position;
  }

  public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor);
  public static boolean outOfBounds(ChessPosition position) {
    if (position.getRow() > 8 || position.getColumn() > 8) {
      return true;
    }
    else{
      return false;
    }
  }
  //Goes as many moves as possible in one direction shown by x and y
  public static void goToStop(ChessPosition myposition, ArrayList<ChessMove> moveslist, ChessBoard board, ChessGame.TeamColor color, int x, int y){
    ChessPosition newposition = new ChessPosition(myposition.getRow(),myposition.getColumn());
    while(true){
      newposition = new ChessPosition(newposition.getRow() + x,newposition.getColumn() + y);
        if(!outOfBounds(newposition)){
          if(board.getPiece(newposition)==null){
            moveslist.add(new ChessMove(myposition,newposition,null));
          }
          else if(!board.getPiece(newposition).getTeamColor().equals(color)){
            moveslist.add(new ChessMove(myposition,newposition,null));
            break;
          }
          else{
            break;
          }
        }
        else{
          break;
        }
    }
    }
  //Goes one move in one direction shown by x and y
  public static void goOneMove(ChessPosition myposition, ArrayList<ChessMove> moveslist, ChessBoard board, ChessGame.TeamColor color, int x, int y){
      ChessPosition newposition = new ChessPosition(myposition.getRow() + x,myposition.getColumn() + y);
      if(!outOfBounds(newposition)){
        if(board.getPiece(newposition)==null){
          moveslist.add(new ChessMove(myposition,newposition,null));
        }
        else if(!board.getPiece(newposition).getTeamColor().equals(color)){
          moveslist.add(new ChessMove(myposition,newposition,null));
        }
      }
    }
}
class King extends ChessMovement{


  public King(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goOneMove(myPosition, alist, board, pieceColor, 0,1);
    goOneMove(myPosition, alist, board, pieceColor, 1,0);
    goOneMove(myPosition, alist, board, pieceColor, 0,-1);
    goOneMove(myPosition, alist, board, pieceColor, -1,0);
    return alist;
  }
}
class Queen extends ChessMovement{

  public Queen(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goToStop(myPosition, alist, board, pieceColor, 0,1);
    goToStop(myPosition, alist, board, pieceColor, 1,1);
    goToStop(myPosition, alist, board, pieceColor, 1,0);
    goToStop(myPosition, alist, board, pieceColor, 1,-1);
    goToStop(myPosition, alist, board, pieceColor, 0,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,0);
    goToStop(myPosition, alist, board, pieceColor, -1,-1);
    return alist;
  }
}
class Bishop extends ChessMovement{

  public Bishop(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goToStop(myPosition, alist, board, pieceColor, 1,1);
    goToStop(myPosition, alist, board, pieceColor, 1,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,-1);
    return alist;
  }
}
class Knight extends ChessMovement{

  public Knight(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goOneMove(myPosition, alist, board, pieceColor, 2,1);
    goOneMove(myPosition, alist, board, pieceColor, 1,2);
    goOneMove(myPosition, alist, board, pieceColor, -2,1);
    goOneMove(myPosition, alist, board, pieceColor, -1,2);
    goOneMove(myPosition, alist, board, pieceColor, 2,-1);
    goOneMove(myPosition, alist, board, pieceColor, 1,-2);
    goOneMove(myPosition, alist, board, pieceColor, -2,-1);
    goOneMove(myPosition, alist, board, pieceColor, -1,-2);
    return alist;
  }
}
class Rook extends ChessMovement{

  public Rook(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goToStop(myPosition, alist, board, pieceColor, 0,1);
    goToStop(myPosition, alist, board, pieceColor, 1,0);
    goToStop(myPosition, alist, board, pieceColor, 0,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,0);
    return alist;
  }
}
class Pawn extends ChessMovement{

  public Pawn(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
    return null;
  }
}
