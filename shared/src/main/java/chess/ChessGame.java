package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.KING;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor teamturn;
    private ChessBoard cboard;
    private ChessBoard lastBoard;

    private boolean gameOver;

    public ChessGame() {
        this.teamturn = WHITE;
        cboard = new ChessBoard();
        cboard.resetBoard();
        gameOver = false;
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

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamturn=" + teamturn +
                ", cboard=" + cboard +
                ", lastBoard=" + lastBoard +
                '}';
    }

    public TeamColor switchColor(TeamColor team){
        return switch(team){
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }

    public ChessPosition findKing(ChessBoard board, ChessGame.TeamColor color){
        ChessPosition kingPosition = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++) {
                if(board.getPiece(new ChessPosition(i,j)) != null) {
                    if (board.getPiece(new ChessPosition(i, j)).getPieceType().equals(KING) && board.getPiece(new ChessPosition(i, j)).getTeamColor().equals(color)) {
                        kingPosition=new ChessPosition(i, j);
                    }
                }
            }
        }
        return kingPosition;
    }
    public void reverseMove(ChessMove move, ChessPiece piecetaken){
        cboard.addPiece(move.getStartPosition(), cboard.getPiece(move.getEndPosition()));
        cboard.addPiece(move.getEndPosition(), piecetaken);
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> allMoves;
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        if(getBoard().getPiece(startPosition).equals(null)){
            return null;
        }
        allMoves = cboard.getPiece(startPosition).pieceMoves(getBoard(), startPosition);
        for(ChessMove move: allMoves){
            cboard.setLastpiecetaken(cboard.getPiece(move.getEndPosition()));
            cboard.addPiece(move.getEndPosition(), cboard.getPiece(move.getStartPosition()));
            cboard.addPiece(move.getStartPosition(), null);
            if(!isInCheck(cboard.getPiece(move.getEndPosition()).getTeamColor())){
                validMoves.add(move);
            }
            reverseMove(move, cboard.getLastpiecetaken());
        }
        return validMoves;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver=gameOver;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start= move.getStartPosition();
        ChessPosition end= move.getEndPosition();
        ChessPiece movingPiece= cboard.getPiece(start);
        Collection<ChessMove> moves;
        moves= validMoves(start);
        if (gameOver == false && getTeamTurn() == movingPiece.getTeamColor() && moves.contains(move)) {
            if(isInStalemate(movingPiece.getTeamColor()) || isInCheckmate(movingPiece.getTeamColor())) {
                if (move.getPromotionPiece() != null) {
                    cboard.addPiece(end, new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
                    cboard.addPiece(start, null);
                    teamturn=switchColor(teamturn);
                } else {
                    cboard.addPiece(end, movingPiece);
                    cboard.addPiece(start, null);
                    teamturn=switchColor(teamturn);
                }
            }
            else{
                throw new InvalidMoveException("Game is now over");
            }
        }
        else {
            throw new InvalidMoveException("Can't make that move");

        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor){
        ChessPosition kingPosition = findKing(cboard, teamColor);
        if(kingPosition == null){
            return false;
        }
        for(ChessPosition piecepositions: getBoard().allColoredPieces(switchColor(teamColor))){
            Collection<ChessMove> validMoves = cboard.getPiece(piecepositions).pieceMoves(cboard, piecepositions);
            for(ChessMove move: validMoves){
                if(move.getEndPosition().equals(kingPosition)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(cboard, teamColor);
        if(isInCheck(teamColor)){
            Collection<ChessMove> moves = validMoves(kingPosition);
            if(moves.size() == 0){
                setGameOver(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(cboard, teamColor);
        if(!isInCheck(teamColor)){
            Collection<ChessMove> moves = validMoves(kingPosition);
            if(moves.size() == 0){
                setGameOver(true);
                return true;
            }
        }
        return false;

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
