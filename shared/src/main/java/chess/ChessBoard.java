package chess;

import java.util.Arrays;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] pieces;
    public ChessBoard() {

        pieces = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return pieces[position.getRow()-1][position.getColumn()-1];
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "pieces=" + Arrays.toString(pieces) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that=(ChessBoard) o;
        return Arrays.deepEquals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for(int i = 0; i < 8; i++){
            pieces[6][i] = new ChessPiece(WHITE,PAWN);
        }
        for(int i = 0; i < 8; i++){
            pieces[1][i] = new ChessPiece(BLACK,PAWN);
        }
        pieces[7][0]= new ChessPiece(WHITE, ROOK);
        pieces[7][1]= new ChessPiece(WHITE, KNIGHT);
        pieces[7][2]= new ChessPiece(WHITE, BISHOP);
        pieces[7][3]= new ChessPiece(WHITE, QUEEN);
        pieces[7][4]= new ChessPiece(WHITE, KING);
        pieces[7][5]= new ChessPiece(WHITE, BISHOP);
        pieces[7][6]= new ChessPiece(WHITE, KNIGHT);
        pieces[7][7]= new ChessPiece(WHITE, ROOK);
        pieces[0][0]= new ChessPiece(BLACK, ROOK);
        pieces[0][1]= new ChessPiece(BLACK, KNIGHT);
        pieces[0][2]= new ChessPiece(BLACK, BISHOP);
        pieces[0][3]= new ChessPiece(BLACK, QUEEN);
        pieces[0][4]= new ChessPiece(BLACK, KING);
        pieces[0][5]= new ChessPiece(BLACK, BISHOP);
        pieces[0][6]= new ChessPiece(BLACK, KNIGHT);
        pieces[0][7]= new ChessPiece(BLACK, ROOK);





    }
}
