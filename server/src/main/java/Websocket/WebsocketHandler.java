package Websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import request.JoinGameRequest;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;


@WebSocket
public class WebsocketHandler {
  private final GameDAO gameDAO;
  private final AuthDAO authDAO;

  private final UserDAO userDAO;

  public WebsocketHandler(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;
    this.userDAO = userDAO;
  }
  private final ConnectionManager connections = new ConnectionManager();
  @OnWebSocketError
  public void onError(Throwable error){
    error.printStackTrace();
  }

  @OnWebSocketClose
  public void close(Session session, int statusCode, String reason) {
// Iterate through connection through connection manager to purge
    connections.cleanConnections();
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
    var conn = connections.getGame(command.getGameID());
    var connPlayer = connections.getConnection(command.getGameID(), command.getAuthString());
    if (conn != null && connPlayer !=null) {
      switch (command.getCommandType()) {
        case JOIN_PLAYER -> sendErrorMsg("Already In Game", connPlayer);
        case JOIN_OBSERVER -> sendErrorMsg("No", connPlayer);
        case MAKE_MOVE -> move(connPlayer, msg);
        case LEAVE -> leave(connPlayer, msg);
        case RESIGN -> resign(connPlayer, msg);
      }
    }
    else {
      if(command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER){
        Connection connection = new Connection(command.getAuthString(), session);
        if(connections.getGame(command.getGameID()) == null){
          connections.addGame(command.getGameID());
          connections.addPlayer(command.getGameID(),command.getAuthString(), connection);
        }
        else{
          connections.addPlayer(command.getGameID(),command.getAuthString(), connection);
        }
        join(msg);
      }
      else if(command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER){
        Connection connection = new Connection(command.getAuthString(), session);
        if(connections.getGame(command.getGameID()) == null){
          connections.addGame(command.getGameID());
          connections.addPlayer(command.getGameID(),command.getAuthString(), connection);
        }
        else{
          connections.addPlayer(command.getGameID(),command.getAuthString(), connection);
        }
        observe(msg);
      }
    }
  }

  public boolean isAuthorized(Connection conn, String authtoken) throws DataAccessException, IOException {
    String messages = "Not Authorized";
    ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, messages);
    if(authDAO.getUser(authtoken) == null){
      conn.send(message);
      return false;
    }
    return true;
  }

  private void join(String msg) throws IOException, DataAccessException {
    JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
    var conn = connections.getConnection(command.getGameID(),command.getAuthString());
    if(isAuthorized(conn, command.getAuthString())){
      GameData game=gameDAO.getGame(command.getGameID());
      AuthData auth = authDAO.getUser(command.getAuthString());
      if(game != null) {
        String wuser = game.getWhiteUsername();
        String buser = game.getBlackUsername();
        if((command.getPlayerColor().equals("WHITE") && wuser == null) || (command.getPlayerColor().equals("BLACK") && buser == null)){
          sendErrorMsg("Empty Team", conn);
        }
        else if((command.getPlayerColor().equals("WHITE") && wuser.equals(auth.getUsername())) || (command.getPlayerColor().equals("BLACK") && buser.equals(auth.getUsername())))
        {
          ChessBoard board=game.getGame().getBoard();
          String message=String.format("%s Player Joined as %s", auth.getUsername(), command.getPlayerColor());
          var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
          var loadgame=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, board, command.getPlayerColor(), game.getGame());
          connections.broadcast(command.getGameID(), conn.authtoken, notification);
          conn.send(loadgame);
        }
        else{
          sendErrorMsg("Wrong Team", conn);
        }
      }
      else{
        sendErrorMsg("Game Doesn't Exist", conn);
      }
    }
  }
  private void observe(String msg) throws IOException, DataAccessException {
    JoinObserver command = new Gson().fromJson(msg, JoinObserver.class);
    var conn = connections.getConnection(command.getGameID(),command.getAuthString());
    if(isAuthorized(conn, command.getAuthString())){
      GameData game=gameDAO.getGame(command.getGameID());
      AuthData auth = authDAO.getUser(command.getAuthString());
      if(game != null) {
        ChessBoard board = game.getGame().getBoard();
        String message=String.format("%s joined as an Observer", auth.getUsername());
        var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        var loadgame=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, board, null, game.getGame());
        connections.broadcast(command.getGameID(), conn.authtoken, notification);
        conn.send(loadgame);
      }
      else{
        sendErrorMsg("Game Doesn't Exist", conn);
      }
    }
  }

  private void move(Connection conn, String msg) throws IOException, DataAccessException, InvalidMoveException {
    MakeMove command = new Gson().fromJson(msg, MakeMove.class);
    if(isAuthorized(conn, command.getAuthString())){
      ChessGame game=gameDAO.getGame(command.getGameID()).getGame();
      ChessGame.TeamColor teamturn = game.getTeamTurn();
      String wuser = gameDAO.getGame(command.getGameID()).getWhiteUsername();
      String buser = gameDAO.getGame(command.getGameID()).getBlackUsername();
      String username = authDAO.getUser(command.getAuthString()).getUsername();
      ChessGame.TeamColor color = null;
      if(username.equals(wuser) && teamturn.equals(ChessGame.TeamColor.WHITE)){
        color = ChessGame.TeamColor.WHITE;
      }
      else if(username.equals(buser) && teamturn.equals(ChessGame.TeamColor.BLACK)){
        color = ChessGame.TeamColor.BLACK;
      }
      else{
        color = null;
      }
      if(game != null && !game.isGameOver()) {
        AuthData auth = authDAO.getUser(command.getAuthString());
        try {
            if(username.equals(wuser) && teamturn.equals(ChessGame.TeamColor.WHITE) || username.equals(buser) && teamturn.equals(ChessGame.TeamColor.BLACK)) {
              if (game.isInCheck(color)) {
                var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is in Check", auth.getUsername()));
                connections.broadcast(command.getGameID(), null, notification);
              }
              else {
                game.makeMove(command.getMove());
                gameDAO.updateGame(command.getGameID(), game);
                String message= "Opponent Made Move: " + command.getMove().toString();
                var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                var loadgame=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.getBoard(), null, game);
                connections.broadcast(command.getGameID(), conn.authtoken, loadgame);
                connections.broadcast(command.getGameID(), conn.authtoken, notification);
                conn.send(loadgame);
              }
            }
            else{
              sendErrorMsg("Not the Correct Player", conn);
            }
        }
        catch(InvalidMoveException ex){
          sendErrorMsg(ex.getMessage(), conn);
        }
      }
      else{
        sendErrorMsg("Game Doesn't Exist", conn);
      }
    }

  }
  public ChessGame.TeamColor switchColor(ChessGame.TeamColor team){
    return switch(team){
      case WHITE -> BLACK;
      case BLACK -> WHITE;
    };
  }
  private void sendErrorMsg(String msg, Connection conn) throws IOException {
    ErrorMessage mes = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, msg);
    conn.send(mes);
  }
  private void resign(Connection conn, String msg) throws DataAccessException, IOException {
    Resign command = new Gson().fromJson(msg, Resign.class);
    String wuser = gameDAO.getGame(command.getGameID()).getWhiteUsername();
    String buser = gameDAO.getGame(command.getGameID()).getBlackUsername();
    String username = authDAO.getUser(command.getAuthString()).getUsername();
    ChessGame game=gameDAO.getGame(command.getGameID()).getGame();
    AuthData auth = authDAO.getUser(command.getAuthString());
    if(!connections.getResignedBoard(command.getGameID()) && (username.equals(wuser) || username.equals(buser))){
      game.setGameOver(true);
      connections.setResigned(command.getGameID());
      gameDAO.updateGame(command.getGameID(), game);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has Resigner, Game Over.", auth.getUsername()));
      connections.broadcast(command.getGameID(), null, notification);
    }
    else{
      sendErrorMsg("Observer's Can't Resign", conn);
    }
  }

  private void leave(Connection conn, String msg) throws DataAccessException, IOException {
    Leave command = new Gson().fromJson(msg, Leave.class);
    String wuser = gameDAO.getGame(command.getGameID()).getWhiteUsername();
    String buser = gameDAO.getGame(command.getGameID()).getBlackUsername();
    String username = authDAO.getUser(command.getAuthString()).getUsername();
    AuthData auth = authDAO.getUser(command.getAuthString());
    int id = command.getGameID();
    String authstring = conn.authtoken;
    if(username.equals(wuser)){
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has left the game.", auth.getUsername()));
      connections.broadcast(id, authstring, notification);
      gameDAO.updateWhiteGame(new JoinGameRequest("WHITE", id), null);
      connections.clearPlayer(id, authstring);
    }
    else if (username.equals(buser)){
      gameDAO.updateWhiteGame(new JoinGameRequest("BLACK", command.getGameID()), null);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has left the game..", auth.getUsername()));
      connections.broadcast(id, command.getAuthString(), notification);
      connections.clearPlayer(id, authstring);
    }
    else{
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has left the game..", auth.getUsername()));
      connections.broadcast(id, authstring, notification);
      connections.clearPlayer(id, authstring);
    }
  }

}
