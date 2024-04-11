package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static ui.EscapeSequences.*;

public class MakeBoard {

  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 1;
  private static final int LINE_WIDTH_IN_CHARS = 1;
  private static final String EMPTY = "  ";
  private static final String X = " X ";
  private static final String O = " O ";
  private static Random rand = new Random();

  public MakeBoard(){
  }

  public static void printBoard(ChessBoard myboard, String color) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    if(color != null && color.equals("WHITE")) {
      drawHeaders(out);
      drawTicTacToeBoard(out, myboard, null);
      drawHeaders(out);
    }
    else if(color != null && color.equals("BLACK")){
      drawHeadersBottom(out);
      drawTicTacToeBoardBottom(out, myboard, null);
      drawHeadersBottom(out);
    }
    else{
      drawHeaders(out);
      drawTicTacToeBoard(out, myboard, null);
      drawHeaders(out);
      drawHeadersBottom(out);
      drawTicTacToeBoardBottom(out, myboard, null);
      drawHeadersBottom(out);
    }

    out.println();
  }

  public static void printMovesBoard(ChessBoard myboard, Collection<ChessMove> moves, String color) {
    Collection<ChessPosition> endpositions = new ArrayList<ChessPosition>();
    for(ChessMove move: moves){
      endpositions.add(move.getEndPosition());
    }
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    drawHeaders(out);
    if(color.equals("WHITE")) {
      drawTicTacToeBoard(out, myboard, endpositions);
    }
    else if(color.equals("BLACK")){
      drawTicTacToeBoardBottom(out, myboard, endpositions);
    }
    drawHeaders(out);
    out.println();
  }

  private static void drawHeaders(PrintStream out) {
    setGrey(out);
    String[] headers = { "    A", "B", "C", "D", "E", "F", "G", "H    "};
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      drawHeader(out, headers[boardCol]);

      if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
        out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
      }
    }
    setBlack(out);
    out.println();
  }
  private static void drawHeadersBottom(PrintStream out) {
    setGrey(out);
    String[] headers = { "    H", "G", "F", "E", "D", "C", "B", "A    "};
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      drawHeader(out, headers[boardCol]);

      if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
        out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
      }
    }
    setBlack(out);
    out.println();
  }

  private static void drawHeader(PrintStream out, String headerText) {
    int prefixLength = 0;
    int suffixLength = 0;
    out.print(EMPTY.repeat(prefixLength));
    printHeaderText(out, headerText);
    out.print(EMPTY.repeat(suffixLength));
  }

  private static void printHeaderText(PrintStream out, String player) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);

    out.print(player);

    setGrey(out);
  }

  private static void drawTicTacToeBoard(PrintStream out, ChessBoard board, Collection<ChessPosition> endpositions) {
    String[] sideheaders = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
    for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++) {
      setGrey(out);
      out.print(sideheaders[boardRow]);
      drawRowOfSquares(out, boardRow, board, endpositions);
      if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
        setGrey(out);
      }
    }
  }
  
  private static void drawTicTacToeBoardBottom(PrintStream out, ChessBoard board, Collection<ChessPosition> endpositions) {
    String[] sideheaders2 = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
    for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++) {
      setGrey(out);
      out.print(sideheaders2[boardRow]);
      drawRowOfSquaresBottom(out, boardRow, board, endpositions);
      if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
        setGrey(out);
      }
    }
  }


  
  private static String getPiece(ChessBoard board, int row, int col){

    ChessPiece piece = board.getPiece(new ChessPosition(row, col));
    if(piece == null){
      return "   ";
    }
    else {
      return switch (piece.getPieceType()) {
        case KING -> " K ";
        case QUEEN -> " Q ";
        case BISHOP -> " B ";
        case KNIGHT -> " N ";
        case ROOK -> " R ";
        case PAWN -> " P ";
        case null -> "   ";

      };
    }
    }
  private static String getPieceColor(ChessBoard board, int row, int col){

    ChessPiece piece = board.getPiece(new ChessPosition(row, col));
    if(piece == null){
      return null;
    }
    else {
      return switch (piece.getTeamColor()) {
        case WHITE -> "Blue";
        case BLACK -> "Red";
        case null -> "   ";

      };
    }
  }


  private static void drawRowOfSquaresBottom(PrintStream out, int row, ChessBoard board, Collection<ChessPosition> endpositions) {
    String[] sideheaders = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
    for(int boardCol=0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      setWhite(out);
      if(endpositions != null && endpositions.contains(new ChessPosition(row+1, boardCol+1))){
          if((row % 2 == 0 && boardCol % 2 == 0) || (row % 2 == 1 && boardCol % 2 == 1)) {
            String color = getPieceColor(board, (row +1), (boardCol+1));
            printPlayerWhiteValid(out, getPiece(board, (row +1), (boardCol+1)), color);
          }
          else if((row % 2 == 1 && boardCol % 2 == 0) || (row % 2 == 0 && boardCol % 2 == 1)){
            String color = getPieceColor(board, row+1, boardCol+1);
            printPlayerBlackValid(out, getPiece(board,row+1, boardCol+1), color);
          }
          else{
            out.println();
          }
        }
        else{
          if((row % 2 == 0 && boardCol % 2 == 0) || (row % 2 == 1 && boardCol % 2 == 1)) {
            String color = getPieceColor(board, (row +1), (boardCol+1));
            printPlayerWhite(out, getPiece(board, (row +1), (boardCol+1)), color);
          }
          else if((row % 2 == 1 && boardCol % 2 == 0) || (row % 2 == 0 && boardCol % 2 == 1)){
            String color = getPieceColor(board, row+1, boardCol+1);
            printPlayerBlack(out, getPiece(board,row+1, boardCol+1), color);
          }
          else{
            out.println();
          }
        }
          setGrey(out);
      }
      setGrey(out);
      out.print(sideheaders[row]);
      setBlack(out);
      out.println();
    }
  private static void drawRowOfSquares(PrintStream out, int row, ChessBoard board, Collection<ChessPosition> endpositions) {
    String[] sideheaders2 = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
    for (int boardCol=0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      setWhite(out);
      if(endpositions != null && endpositions.contains(new ChessPosition(row+1, boardCol+1))) {
        if ((row % 2 == 0 && boardCol % 2 == 0) || (row % 2 == 1 && boardCol % 2 == 1)) {
          String color=getPieceColor(board, 9 - (row + 1), 9 - (boardCol + 1));
          printPlayerWhiteValid(out, getPiece(board, 9 - (row + 1), 9 - (boardCol + 1)), color);
        }
        else if ((row % 2 == 1 && boardCol % 2 == 0) || (row % 2 == 0 && boardCol % 2 == 1)) {
          String color=getPieceColor(board, 9 - (row + 1), 9 - (boardCol + 1));
          printPlayerBlackValid(out, getPiece(board, 9 - (row + 1), 9 - (boardCol + 1)), color);
        }
        else {
          out.println();
        }
      }
      else{
        if ((row % 2 == 0 && boardCol % 2 == 0) || (row % 2 == 1 && boardCol % 2 == 1)) {
          String color=getPieceColor(board, 9 - (row + 1), 9 - (boardCol + 1));
          printPlayerWhite(out, getPiece(board, 9 - (row + 1), 9 - (boardCol + 1)), color);
        }
        else if ((row % 2 == 1 && boardCol % 2 == 0) || (row % 2 == 0 && boardCol % 2 == 1)) {
          String color=getPieceColor(board, 9 - (row + 1), 9 - (boardCol + 1));
          printPlayerBlack(out, getPiece(board, 9 - (row + 1), 9 - (boardCol + 1)), color);
        }
        else {
          out.println();
        }
      }
      setGrey(out);
    }
    setGrey(out);
    out.print(sideheaders2[row]);
    setBlack(out);
    out.println();
  }


  private static void setWhite(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void setGrey(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
  }
  private static void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLACK);
  }


  private static void printPlayerWhite(PrintStream out, String player, String color) {
    out.print(SET_BG_COLOR_WHITE);
    if(color == null){
    }
    else {
      if (color.equals("Blue")) {
        out.print(SET_TEXT_COLOR_BLUE);
      } else if (color.equals("Red")) {
        out.print(SET_TEXT_COLOR_RED);
      } else {
      }
    }

    out.print(player);

    setWhite(out);
  }
  private static void printPlayerWhiteValid(PrintStream out, String player, String color) {
    out.print(SET_BG_COLOR_GREEN);
    if(color == null){
    }
    else {
      if (color.equals("Blue")) {
        out.print(SET_TEXT_COLOR_BLUE);
      } else if (color.equals("Red")) {
        out.print(SET_TEXT_COLOR_RED);
      } else {
      }
    }

    out.print(player);

    setWhite(out);
  }


  private static void printPlayerBlack(PrintStream out, String player, String color) {
    out.print(SET_BG_COLOR_BLACK);
    if(color == null){
    }
    else {
      if (color.equals("Blue")) {
        out.print(SET_TEXT_COLOR_BLUE);
      } else if (color.equals("Red")) {
        out.print(SET_TEXT_COLOR_RED);
      } else {
      }
    }

    out.print(player);

    setWhite(out);
  }
  private static void printPlayerBlackValid(PrintStream out, String player, String color) {
    out.print(SET_BG_COLOR_DARK_GREEN);
    if(color == null){
    }
    else {
      if (color.equals("Blue")) {
        out.print(SET_TEXT_COLOR_BLUE);
      } else if (color.equals("Red")) {
        out.print(SET_TEXT_COLOR_RED);
      } else {
      }
    }

    out.print(player);

    setWhite(out);
  }
}