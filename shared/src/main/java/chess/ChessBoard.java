package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {

        ChessPiece[][] pieces = new ChessPiece[8][8]
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces[position.getRow][position.getColum] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return pieces[position.getRow][position.getColum];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for(int i = 0, i < 8, i++){
            pieces[1][i] = ChessPiece(PAWN,WHITE)
        }
        for(int i = 0, i < 8, i++){
            pieces[6][i] = ChessPiece(PAWN,BLACK)
        }
        pieces[0][0]=ChessPiece(ROOK,WHITE)
        pieces[0][1]=ChessPiece(KNIGHT,WHITE)
        pieces[0][2]=ChessPiece(BISHOP,WHITE)
        pieces[0][3]=ChessPiece(QUEEN,WHITE)
        pieces[0][4]=ChessPiece(KING,WHITE)
        pieces[0][5]=ChessPiece(BISHOP,WHITE)
        pieces[0][6]=ChessPiece(KNIGHT,WHITE)
        pieces[0][7]=ChessPiece(ROOK,WHITE)
        pieces[7][0]=ChessPiece(ROOK,BLACK)
        pieces[7][1]=ChessPiece(KNIGHT,BLACK)
        pieces[7][2]=ChessPiece(BISHOP,BLACK)
        pieces[7][3]=ChessPiece(QUEEN,BLACK)
        pieces[7][4]=ChessPiece(KING,BLACK)
        pieces[7][5]=ChessPiece(BISHOP,BLACK)
        pieces[7][6]=ChessPiece(KNIGHT,BLACK)
        pieces[7][7]=ChessPiece(ROOK,BLACK)





    }
}
