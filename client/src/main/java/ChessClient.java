import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.ListGamesResponse;
import server.ServerFacade;
import ui.MakeBoard;

import java.util.Arrays;
import java.util.Collection;


public class ChessClient {
  private String visitorName = null;
  private final ServerFacade server;
  private final String serverUrl;
  private State state = State.SIGNEDOUT;

  private MakeBoard makeboard;

  private ChessGame newgame;

  private String joinGameColor;
  private boolean inGame;
  private String authtoken;
  public ChessClient(String serverUrl, Repl repl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    inGame = false;
    newgame = new ChessGame();
    makeboard = new MakeBoard();
  }

  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "login" -> signIn(params);
        case "register" -> register(params);
        case "logout" -> signOut();
        case "creategame" -> createGame(params);
        case "listgames" -> listGames();
        case "joingame" -> joinGame(params);
        case "joinobserver" -> joinGameObserver(params);
        case "help" -> help();
        case "quit" -> "quit";
        default -> help();
      };
    } catch (DataAccessException ex) {
      return ex.getMessage();
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public String evalgame(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "redrawboard" -> redrawBoard();
        case "leave" -> leave();
        case "makemove" -> makeMove(params);
        case "resign" -> listGames();
        case "highlightlegalmoves" -> highlightMoves(params);
        case "help" -> helpgame();
        default -> helpgame();
      };
    } catch (DataAccessException ex) {
      return ex.getMessage();
    } catch (Exception e) {
      return e.getMessage();
    }
  }
  public String redrawBoard() throws Exception {
    assertSignedIn();
    ChessBoard myboard = new ChessBoard();
    myboard.resetBoard();
    makeboard.printBoard(myboard, joinGameColor);
    return String.format("Redraw of Board");
  }

  public String leave() throws Exception {
    assertSignedIn();
    inGame = false;
    return String.format("%s left the chess game", visitorName);
  }
  public String highlightMoves(String... params) throws Exception {
    if (params.length >= 2) {
      assertSignedIn();
      var columnname= params[0].toUpperCase();
      var row = Integer.parseInt(params[1]);
      if(joinGameColor.equals("WHITE")){
        row = 9-row;
      }
      int column = getColumnNumber(columnname);
      ChessBoard myboard = new ChessBoard();
      myboard.resetBoard();
      ChessGame game = new ChessGame();
      makeboard.printMovesBoard(myboard, game.validMoves(new ChessPosition(row, column)), joinGameColor);
      return String.format("All Moves Highlighted in Green are available");
    }
    throw new DataAccessException("Expected: <username>");
  }

  public String makeMove(String... params) throws Exception {
    if (params.length >= 4) {
      assertSignedIn();
      var columnname1= params[0].toUpperCase();
      var row1 = Integer.parseInt(params[1]);
      var columnname2= params[2].toUpperCase();
      var row2 = Integer.parseInt(params[3]);
      int column1=getColumnNumber(columnname1);
      int column2=getColumnNumber(columnname2);
      if(joinGameColor.equals("WHITE")) {
        column1 -=9;
        column2 -=9;
      }
      ChessPosition start = new ChessPosition(row1, column1);
      ChessPosition end = new ChessPosition(row2, column2);
      ChessMove move = new ChessMove(start, end, null);
      newgame.makeMove(move);
      makeboard.printBoard(newgame.getBoard(), joinGameColor);
      return String.format("You moved from %s%d to %s%d", columnname1, row1, columnname2, row2);
    }
    throw new DataAccessException("Expected: <username>");
  }

  public String signIn(String... params) throws Exception {
    if (params.length >= 2) {
      state = State.SIGNEDIN;
      visitorName = String.join("-", params);
      var name= params[0];
      var password = params[1];
      AuthData value = server.login(new LoginRequest(name, password));
      authtoken = value.getAuthToken();
      return String.format("You signed in as %s.", visitorName);
    }
    throw new DataAccessException("Expected: <username>");
  }
  public String register(String... params) throws Exception {
    if (params.length >= 3) {
      state = State.SIGNEDIN;
      var name= params[0];
      var password = params[1];
      var email = params[2];
      visitorName = String.join("-", params);
      AuthData value = server.register(new RegisterRequest(name, password, email));
      authtoken = value.getAuthToken();
      return String.format("You registered in as %s.", visitorName);
    }
    throw new DataAccessException("Expected: <username> <password> <email>");
  }
  public String signOut() throws Exception {
    assertSignedIn();
    state = State.SIGNEDOUT;
    server.logout(authtoken);
    authtoken = null;
    return String.format("%s left the chess portal", visitorName);
  }
  public String createGame(String... params) throws Exception {
    assertSignedIn();
    if (params.length >= 1) {
      var name=params[0];
      server.createGame(new CreateGameRequest(name), authtoken);
      return String.format("New game: %s. Created", name);
    }
    throw new DataAccessException("Expected: <gamename>");
  }
  public String listGames() throws Exception {
      assertSignedIn();
      ListGamesResponse response = server.listGames(authtoken);
      Collection<GameData> games = response.games();
      System.out.println("Listing games...");
      int i = 1;
      for(GameData x: games){
        System.out.print(i);
        System.out.println(x.toString());
        i++;
      }
      return String.format("All games listed");
    }
  public String joinGame(String... params) throws Exception {
    assertSignedIn();
    if (params.length >= 2) {
      var color= params[0].toUpperCase();
      var gameID = Integer.parseInt(params[1]);
      joinGameColor = color;
      server.joinGame(new JoinGameRequest(color, gameID), authtoken);
      makeboard.printBoard(newgame.getBoard(), color);
      inGame = true;
      return String.format("Joined game %d as: %s. ", gameID, color);
    }
    throw new DataAccessException("Expected: <player color WHITE|BLACK> <game ID>");
  }
  public String joinGameObserver(String... params) throws Exception {
    assertSignedIn();
    if (params.length >= 1) {
      var gameID = Integer.parseInt(params[0]);
      server.joinGame(new JoinGameRequest(null, gameID), authtoken);
      makeboard.printBoard(newgame.getBoard(), "WHITE");
      makeboard.printBoard(newgame.getBoard(), "BLACK");
      return String.format("Joined game %d as an observer", gameID);
    }
    throw new DataAccessException("Expected: <game ID>");
  }

  public String help() {
    if (state == State.SIGNEDOUT) {
      return """
                    - login <username>
                    - register <username> <password> <email>
                    - quit
                    - help
                    """;
    }
    return """
                - logout
                - creategame <game name>
                - listgames
                - joingame <player color WHITE|BLACK> <game ID>
                - joinobserver <game ID>
                - quit
                - help
                """;
  }
  public String helpgame() {
    return """
                - redrawboard
                - leave
                - makemove <letter> <number> <letter> <number>
                - resign <YES or NO>
                - higlightlegalmoves <letter> <number>
                - help
                """;
  }
  private void assertSignedIn() throws DataAccessException {
    if (state == State.SIGNEDOUT) {
      throw new DataAccessException("You must sign in");
    }
  }

  public boolean isInGame() {
    return inGame;
  }

  private static int getColumnNumber(String val){


    if(val == null){
      return 0;
    }
    else {
      return switch (val) {
        case "A" -> 1;
        case "B" -> 2;
        case "C" -> 3;
        case "D" -> 4;
        case "E" -> 5;
        case "F" -> 6;
        case "G" -> 7;
        case "H" -> 8;
        case null -> 0;
        default -> 0;

      };
    }
  }
}
