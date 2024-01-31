package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.PAWN;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor teamturn;
    private ChessBoard cboard;

    public ChessGame() {
        this.teamturn = WHITE;
        cboard = new ChessBoard();
        cboard.resetBoard();
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamturn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamturn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
    public TeamColor switchTurn(TeamColor team){
        return switch(team){
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }

    public ChessPosition findKing(ChessBoard board, ChessGame.TeamColor color){
        ChessPosition kingPosition = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 0; j <= 8; j++) {
                if(board.getPiece(new ChessPosition(i,j)).getPieceType().equals(KING) && board.getPiece(new ChessPosition(i,j)).getTeamColor().equals(color)){
                     kingPosition = new ChessPosition(i,j);
                }
            }
        }
        return kingPosition;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //return null if nothing is there
        if(getBoard().getPiece(startPosition) == null){
            return null;
        }
        else {
            return getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition);
        }
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @param board board to perform the move on
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move, ChessBoard board) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPiece movingPiece = board.getPiece(start);
        ChessPosition end = move.getendPosition();
        Collection<ChessMove> moves;
        moves = validMoves(start);
        for(ChessMove eachMove: moves){
            if(getTeamTurn() == movingPiece.getTeamColor() && eachMove.getendPosition() == end && !isInCheck(movingPiece.getTeamColor())){
                board.addPiece(end, movingPiece);
            }
            else{
                throw new InvalidMoveException("Can't make that move");
            }
        }

    }
    public ChessBoard copyBoardMove(ChessMove move) throws InvalidMoveException {
        ChessBoard newboard = new ChessBoard();
        for(int i = 1; i <= 8; i++){
            for(int j = 0; j <= 8; j++) {
                newboard.addPiece(new ChessPosition(i,j), getBoard().getPiece(new ChessPosition(i,j)));
            }
        }
        try {
            makeMove(move,newboard);
        }
        catch (InvalidMoveException){
            return null;
        }
        return newboard;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(getBoard(),teamColor);
        ArrayList<ChessBoard> possibleBoards = new ArrayList<ChessBoard>();
        ArrayList<ChessMove> allMoves= new ArrayList<ChessMove>();
        for(int i = 1; i <= 8; i++){
            for(int j = 0; j <= 8; j++) {
               if(getBoard().getPiece(new ChessPosition(i,j)).getTeamColor() == switchTurn(teamColor)){
                   allMoves.add(getBoard().getPiece(new ChessPosition(i,j))
               }
            }
        }



    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        cboard = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return cboard;
    }
}
