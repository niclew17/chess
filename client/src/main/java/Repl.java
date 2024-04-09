import Websocket.NotificationHandler;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;
public class Repl implements NotificationHandler {
  private final ChessClient client;

  public Repl(String serverUrl) {
    client=new ChessClient(serverUrl, this);
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
  public void notify(ServerMessage message) {
    switch (message.getServerMessageType()) {
      case NOTIFICATION -> displayNotification(((Notification) message).getMessage());
      case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
      case LOAD_GAME -> loadGame(((LoadGame) message).getGame());
    }
  }

  public void displayNotification(String message){
    System.out.println(SET_BG_COLOR_DARK_GREEN + message);
    printPrompt();
  }
  public void displayError(String message){
    System.out.println(SET_BG_COLOR_RED + message);
    printPrompt();
  }
  public void loadGame(boolean message){
    System.out.println(SET_BG_COLOR_DARK_GREEN);
    printPrompt();
  }


}
