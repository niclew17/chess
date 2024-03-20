package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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

//  public static void main(String[] args){
//    printBoard();
//  }
  public static void printBoard() {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    drawHeaders(out);
    drawTicTacToeBoard(out);
    drawHeaders(out);
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
    out.println();
    drawHeadersBottom(out);
    drawTicTacToeBoardBottom(out);
    drawHeadersBottom(out);
  }

  private static void drawHeaders(PrintStream out) {
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
  private static void drawHeadersBottom(PrintStream out) {
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

  private static void drawTicTacToeBoard(PrintStream out) {
    String[] sideheaders = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
    for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++) {
      setGrey(out);
      out.print(sideheaders[boardRow]);
      drawRowOfSquares(out, boardRow);
      if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
        setGrey(out);
      }
    }
  }
  private static void drawTicTacToeBoardBottom(PrintStream out) {
    String[] sideheaders2 = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
    for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++) {
      setGrey(out);
      out.print(sideheaders2[boardRow]);
      drawRowOfSquaresBottom(out, boardRow);
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
        case WHITE -> "Red";
        case BLACK -> "Blue";
        case null -> "   ";

      };
    }
  }


  private static void drawRowOfSquares(PrintStream out, int row) {
    String[] sideheaders = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
    ChessBoard cleanboard=new ChessBoard();
    cleanboard.resetBoard();
    for (int boardCol=0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      setWhite(out);
        if((row % 2 == 0 && boardCol % 2 == 0) || (row % 2 == 1 && boardCol % 2 == 1)) {
          String color = getPieceColor(cleanboard, row +1, boardCol+1);
          printPlayerWhite(out, getPiece(cleanboard, row +1, boardCol+1), color);
        }
        else if((row % 2 == 1 && boardCol % 2 == 0) || (row % 2 == 0 && boardCol % 2 == 1)){
          String color = getPieceColor(cleanboard, row+1, boardCol+1);
          printPlayerBlack(out, getPiece(cleanboard, row+1, boardCol+1), color);
        }
        else{
          out.println();
        }
          setGrey(out);
      }
      setGrey(out);
      out.print(sideheaders[row]);
      setBlack(out);
      out.println();
    }
  private static void drawRowOfSquaresBottom(PrintStream out, int row) {
    String[] sideheaders2 = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
    ChessBoard cleanboard=new ChessBoard();
    cleanboard.resetBoard();
    for (int boardCol=0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      setWhite(out);
      if((row % 2 == 0 && boardCol % 2 == 0) || (row % 2 == 1 && boardCol % 2 == 1)) {
        String color = getPieceColor(cleanboard, 9-(row +1), 9-(boardCol+1));
        printPlayerWhite(out, getPiece(cleanboard, 9-(row +1), 9-(boardCol+1)), color);
      }
      else if((row % 2 == 1 && boardCol % 2 == 0) || (row % 2 == 0 && boardCol % 2 == 1)){
        String color = getPieceColor(cleanboard, 9-(row+1), 9-(boardCol+1));
        printPlayerBlack(out, getPiece(cleanboard, 9-(row+1), 9-(boardCol+1)), color);
      }
      else{
        out.println();
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

  private static void setRed(PrintStream out) {
    out.print(SET_BG_COLOR_RED);
    out.print(SET_TEXT_COLOR_RED);
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
}