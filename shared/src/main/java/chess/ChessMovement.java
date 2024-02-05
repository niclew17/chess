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
  private ChessGame.TeamColor pieceColor;
  private ChessBoard board;
  private ChessPosition position;


  public ChessMovement(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    this.pieceColor=pieceColor;
    this.board = board;
    this.position = position;
  }

  public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor);
  public static boolean outOfBounds(ChessPosition position) {
    if (position.getRow() > 8 || position.getColumn() > 8 || position.getRow() < 1 || position.getColumn() < 1) {
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
  public static void goFirstMovePawn(ChessPosition myposition, ArrayList<ChessMove> moveslist, ChessBoard board, ChessGame.TeamColor color, int x) {
    ChessPosition newposition=new ChessPosition(myposition.getRow() + x, myposition.getColumn());
    if (x == 2 || x==-2) {
      ChessPosition checkposition=new ChessPosition(myposition.getRow() + (x/2), myposition.getColumn());
      if (!outOfBounds(newposition)) {
        if (board.getPiece(checkposition) == null && board.getPiece(newposition) == null) {
          moveslist.add(new ChessMove(myposition, newposition, null));
        }
      }
    }
    else {
      if (!outOfBounds(newposition)) {
        if (board.getPiece(newposition) == null) {
          moveslist.add(new ChessMove(myposition, newposition, null));
        }
      }
    }
  }

  public static boolean notPromotingPiece(ChessPosition myposition, ChessPosition endposition, ChessGame.TeamColor color, ArrayList<ChessMove> alist){
    if(endposition.getRow() == 8 && color.equals(ChessGame.TeamColor.WHITE)){
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.KNIGHT));
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.QUEEN));
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.BISHOP));
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.ROOK));
      return false;
      }
    else if(endposition.getRow() == 1 && color.equals(ChessGame.TeamColor.BLACK)){
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.KNIGHT));
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.QUEEN));
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.BISHOP));
      alist.add(new ChessMove(myposition, endposition, ChessPiece.PieceType.ROOK));
      return false;
    }
    else{
      return true;
    }
  }

  public static void goOneMovePawn(ChessPosition myposition, ArrayList<ChessMove> moveslist, ChessBoard board, ChessGame.TeamColor color, int z){
    ChessPosition newposition = new ChessPosition(myposition.getRow() + z, myposition.getColumn());
    ChessPosition attackleft = new ChessPosition(myposition.getRow() + z, myposition.getColumn() + z );
    ChessPosition attackright = new ChessPosition(myposition.getRow() + z, myposition.getColumn() - z );
    if(!outOfBounds(newposition)) {
      if (board.getPiece(newposition) == null) {
        if (notPromotingPiece(myposition, newposition, color, moveslist)) {
          moveslist.add(new ChessMove(myposition, newposition, null));
        }
      }
    }
    if(!outOfBounds(attackleft)) {
      if (board.getPiece(attackleft) != null && !board.getPiece(attackleft).getTeamColor().equals(color)) {
        if (notPromotingPiece(myposition, attackleft, color, moveslist)) {
          moveslist.add(new ChessMove(myposition, attackleft, null));
        }
      }
    }
    if(!outOfBounds(attackright)) {
      if (board.getPiece(attackright) != null && !board.getPiece(attackright).getTeamColor().equals(color)) {
        if (notPromotingPiece(myposition, attackright, color, moveslist)) {
          moveslist.add(new ChessMove(myposition, attackright, null));
        }
      }
    }
  }
}

class King extends ChessMovement{


  public King(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goOneMove(myPosition, alist, board, pieceColor, 0,1);
    goOneMove(myPosition, alist, board, pieceColor, 1,1);
    goOneMove(myPosition, alist, board, pieceColor, 1,0);
    goOneMove(myPosition, alist, board, pieceColor, 1,-1);
    goOneMove(myPosition, alist, board, pieceColor, 0,-1);
    goOneMove(myPosition, alist, board, pieceColor, -1,-1);
    goOneMove(myPosition, alist, board, pieceColor, -1,0);
    goOneMove(myPosition, alist, board, pieceColor, -1,1);
    return alist;
  }
}
class Queen extends ChessMovement{

  public Queen(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goToStop(myPosition, alist, board, pieceColor, 0,1);
    goToStop(myPosition, alist, board, pieceColor, 1,1);
    goToStop(myPosition, alist, board, pieceColor, 1,0);
    goToStop(myPosition, alist, board, pieceColor, 1,-1);
    goToStop(myPosition, alist, board, pieceColor, 0,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,0);
    goToStop(myPosition, alist, board, pieceColor, -1,1);
    return alist;
  }
}
class Bishop extends ChessMovement{

  public Bishop(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    goToStop(myPosition, alist, board, pieceColor, 1,1);
    goToStop(myPosition, alist, board, pieceColor, 1,-1);
    goToStop(myPosition, alist, board, pieceColor, -1,1);
    goToStop(myPosition, alist, board, pieceColor, -1,-1);
    return alist;
  }
}
class Knight extends ChessMovement{

  public Knight(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    super(pieceColor, board, position);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
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
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
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
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    ArrayList<ChessMove> alist=new ArrayList<ChessMove>();
    int forwardmove = 0;
    if(pieceColor.equals(ChessGame.TeamColor.BLACK) && myPosition.getRow()!=7){
      forwardmove = -1;
      goOneMovePawn(myPosition,alist,board,pieceColor, forwardmove);
    }
    else if(pieceColor.equals(ChessGame.TeamColor.WHITE) && myPosition.getRow()!=2) {
      forwardmove = 1;
      goOneMovePawn(myPosition,alist,board,pieceColor, forwardmove);
    }
    else if(pieceColor.equals(ChessGame.TeamColor.BLACK) && myPosition.getRow()==7) {
      goFirstMovePawn(myPosition, alist, board, pieceColor, -1);
      goFirstMovePawn(myPosition, alist, board, pieceColor, -2);
    }
    else if(pieceColor.equals(ChessGame.TeamColor.WHITE) && myPosition.getRow()==2) {
      goFirstMovePawn(myPosition, alist, board, pieceColor, 1);
      goFirstMovePawn(myPosition, alist, board, pieceColor, 2);

    }
    return alist;
    }



  }

