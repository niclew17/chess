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
  private String authtoken;
  public ChessClient(String serverUrl, Repl repl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
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
      MakeBoard board = new MakeBoard();
      board.printBoard();
      return String.format("New game: %s. Created", name);
    }
    throw new DataAccessException("Expected: <gamename>");
  }
  public String listGames() throws Exception {
      assertSignedIn();
      ListGamesResponse response = server.listGames(authtoken);
      Collection<GameData> games = response.games();
      System.out.println("Listing games...");
      for(GameData x: games){
        System.out.println(x.toString());
      }
      return String.format("All games listed");
    }
  public String joinGame(String... params) throws Exception {
    assertSignedIn();
    if (params.length >= 2) {
      var color= params[0].toUpperCase();
      var gameID = Integer.parseInt(params[1]);
      server.joinGame(new JoinGameRequest(color, gameID), authtoken);
      return String.format("Joined game %d as: %s. ", gameID, color);
    }
    throw new DataAccessException("Expected: <player color WHITE|BLACK> <game ID>");
  }
  public String joinGameObserver(String... params) throws Exception {
    assertSignedIn();
    if (params.length >= 1) {
      var gameID = Integer.parseInt(params[0]);
      server.joinGame(new JoinGameRequest(null, gameID), authtoken);
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
  private void assertSignedIn() throws DataAccessException {
    if (state == State.SIGNEDOUT) {
      throw new DataAccessException("You must sign in");
    }
  }



}
