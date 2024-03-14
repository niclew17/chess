package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class MakeBoard {
  private static final int BOARD_SIZE_IN_SQUARES = 3;
  private static final int SQUARE_SIZE_IN_CHARS = 3;
  private static final int LINE_WIDTH_IN_CHARS = 1;
  private static final String EMPTY = "   ";
  private static final String X = " X ";
  private static final String O = " O ";



  private static void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLACK);
  }
  private static void setBlackRed(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_RED);
  }
  private static void setBlackBlue(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLUE);
  }
  private static void setWhiteRed(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_RED);
  }
  private static void setWhiteBlue(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_BLUE);
  }
  private static void setWhite(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_WHITE);
  }
  private static void setGrey(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_LIGHT_GREY);
  }
  private static void setGreyBlack(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
  }
  private static void drawFirstHeaderRow(PrintStream out) {
    setGrey(out);
    

  }
  private static void drawSecondHeaderRow(PrintStream out) {

  }
  private static void drawFirstHeaderColumn(PrintStream out) {

  }
  private static void drawSecondHeaderColumn(PrintStream out) {

  }



}

