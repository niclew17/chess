package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType type;
    private ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;

    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;

    }

    @Override
    public String toString() {
        return "t=" + type +
                ", c=" + pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
     return switch (board.getPiece(myPosition).getPieceType()) {
       case KING -> new King(board.getPiece(myPosition).getTeamColor(), board, myPosition).pieceMoves(board, myPosition, board.getPiece(myPosition).getTeamColor());
       case QUEEN -> new Queen(board.getPiece(myPosition).getTeamColor(), board, myPosition).pieceMoves(board, myPosition, board.getPiece(myPosition).getTeamColor());
       case BISHOP -> new Bishop(board.getPiece(myPosition).getTeamColor(), board, myPosition).pieceMoves(board, myPosition, board.getPiece(myPosition).getTeamColor());
       case KNIGHT -> new Knight(board.getPiece(myPosition).getTeamColor(), board, myPosition).pieceMoves(board, myPosition, board.getPiece(myPosition).getTeamColor());
       case ROOK -> new Rook(board.getPiece(myPosition).getTeamColor(), board, myPosition).pieceMoves(board, myPosition, board.getPiece(myPosition).getTeamColor());
       case PAWN -> new Pawn(board.getPiece(myPosition).getTeamColor(), board, myPosition).pieceMoves(board, myPosition, board.getPiece(myPosition).getTeamColor());

     };

    }
}
