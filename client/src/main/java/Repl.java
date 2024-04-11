import Websocket.NotificationHandler;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import ui.MakeBoard;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;
public class Repl implements NotificationHandler {
  private final ChessClient client;

  private MakeBoard makeboard;

  public Repl(String serverUrl) throws DataAccessException {
    client=new ChessClient(serverUrl, this);
    makeboard = new MakeBoard();
  }

  public void run() {
    System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.");
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
        printPrompt();
        String line=scanner.nextLine();
        if(!client.isInGame()) {
          try {
            result=client.eval(line);
            System.out.print(SET_TEXT_COLOR_BLUE + result);
          } catch (Throwable e) {
            var msg=e.toString();
            System.out.print(msg);
          }
        }
        else{
          try {
            result=client.evalgame(line);
            System.out.print(SET_TEXT_COLOR_BLUE + result);
          } catch (Throwable e) {
            var msg=e.toString();
            System.out.print(msg);
          }
        }
      }

    System.out.println();
  }
  private void printPrompt() {
    System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
  }

  @Override
  public void notify(ServerMessage message, String messages) {
    switch (message.getServerMessageType()) {
      case NOTIFICATION -> displayNotification(messages);
      case ERROR -> displayError(messages);
      case LOAD_GAME -> loadGame(messages);
    }
  }


  public void displayNotification(String message){
    Notification command = new Gson().fromJson(message, Notification.class);
    System.out.println("");
    System.out.println(SET_BG_COLOR_DARK_GREEN + command.getMessage());
    printPrompt();
  }
  public void displayError(String message){
    Error command = new Gson().fromJson(message, Error.class);
    System.out.println("");
    System.out.println(SET_BG_COLOR_RED + command.getMessage());
    printPrompt();
  }
  public void loadGame(String message){
    LoadGame command = new Gson().fromJson(message, LoadGame.class);
    client.setGameboard(command.getBoard());
    client.setMygame(command.getGame());
    System.out.println("");
    makeboard.printBoard(command.getBoard(), command.getColor());
    printPrompt();
  }


}
