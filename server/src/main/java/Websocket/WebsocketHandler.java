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
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.EncodeException;
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
        case JOIN_PLAYER -> connPlayer.send(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Already In a Game"));
        case JOIN_OBSERVER -> sendErrorMsg("No", connPlayer);
        case MAKE_MOVE -> move(connPlayer, msg); //move(conn, msg);
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

  public boolean isAuthorized(Connection conn, String authtoken) throws DataAccessException, EncodeException, IOException {
    String messages = "Not Authorized";
    ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, messages);
    if(authDAO.getUser(authtoken) == null){
      conn.send(message);
      return false;
    }
    return true;
  }

  private void join(String msg) throws IOException, EncodeException, DataAccessException {
    JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
    var conn = connections.getConnection(command.getGameID(),command.getAuthString());
    if(isAuthorized(conn, command.getAuthString())){
      String message=String.format("%s Player Joined", command.getPlayerColor());
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
          var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
          var load_game=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, board, command.getPlayerColor());
          connections.broadcast(command.getGameID(), conn.authtoken, notification);
          conn.send(load_game);
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
  private void observe(String msg) throws EncodeException, IOException, DataAccessException {
    JoinObserver command = new Gson().fromJson(msg, JoinObserver.class);
    var conn = connections.getConnection(command.getGameID(),command.getAuthString());
    if(isAuthorized(conn, command.getAuthString())){
      String message= "Observer Joined";
      GameData game=gameDAO.getGame(command.getGameID());
      if(game != null) {
        ChessBoard board = game.getGame().getBoard();
        var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        var load_game=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, board, null);
        connections.broadcast(command.getGameID(), conn.authtoken, notification);
        conn.send(load_game);
      }
      else{
        sendErrorMsg("Game Doesn't Exist", conn);
      }
    }
  }

  private void move(Connection conn, String msg) throws EncodeException, IOException, DataAccessException, InvalidMoveException {
    MakeMove command = new Gson().fromJson(msg, MakeMove.class);
    if(isAuthorized(conn, command.getAuthString())){
      String message= "Opponent Made Move: " + command.getMove().toString();
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
      if(game != null) {
        AuthData auth = authDAO.getUser(command.getAuthString());
        try {
            if(username.equals(wuser) && teamturn.equals(ChessGame.TeamColor.WHITE) || username.equals(buser) && teamturn.equals(ChessGame.TeamColor.BLACK)) {
              if (game.isInCheck(color)) {
                var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is in Check", auth.getUsername()));
                connections.broadcast(command.getGameID(), null, notification);
              } else if (game.isInCheckmate(switchColor(color)) || game.isInCheckmate(color)){
                var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is in Checkmate, Game Over.", auth.getUsername()));
                connections.broadcast(command.getGameID(), null, notification);
              } else {
                game.makeMove(command.getMove());
                gameDAO.updateGame(command.getGameID(), game);
                var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                var load_game=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.getBoard(), null);
                connections.broadcast(command.getGameID(), conn.authtoken, load_game);
                connections.broadcast(command.getGameID(), conn.authtoken, notification);
                conn.send(load_game);
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
  private void sendErrorMsg(String msg, Connection conn) throws EncodeException, IOException {
    ErrorMessage mes = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, msg);
    conn.send(mes);
  }
  private void leave(Connection conn, String msg){

  }
  private void resign(Connection conn, String msg){

  }

}
