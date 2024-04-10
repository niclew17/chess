package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO {


  public MySQLGameDAO() throws DataAccessException {
    configureDatabase();
  }
  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            );
            """
  };


  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }

  public CreateGameResponse createGame(CreateGameRequest game) throws DataAccessException {
    var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
    var json = new Gson().toJson(new ChessGame());
    var gameID = executeUpdate(statement, null, null, game.gameName(), json);
    return new CreateGameResponse(gameID);
  }

  public ListGamesResponse listGames() throws DataAccessException{
    var result = new ArrayList<GameData>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            result.add(readGame(rs));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return new ListGamesResponse(result);
  }
  private GameData readGame(ResultSet rs) throws SQLException{
    var gameID= rs.getInt("gameID");
    var wUsername = rs.getString("whiteUsername");
    var bUsername = rs.getString("blackUsername");
    var gameName = rs.getString("gameName");
    var json = rs.getString("game");
    var chessGame = new Gson().fromJson(json, ChessGame.class);
    return new GameData(gameID,wUsername,bUsername,gameName,chessGame);
  }

  public GameData getGame(int id) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setInt(1, id);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readGame(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  public void updateGame(int gameID, ChessGame game) throws DataAccessException {
    try (var conn=DatabaseManager.getConnection()) {
      var statement="UPDATE games SET game=? WHERE gameID=?";
      try (var ps=conn.prepareStatement(statement)) {
        var json = new Gson().toJson(game);
        ps.setString(1, json);
        ps.setInt(2, gameID);
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  public void updateBlackGame(JoinGameRequest game, String username) throws DataAccessException {
    try (var conn=DatabaseManager.getConnection()) {
      var statement="UPDATE games SET blackUsername=? WHERE gameID=?";
      try (var ps=conn.prepareStatement(statement)) {
        ps.setString(1, username);
        ps.setInt(2, game.gameID());
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }
  public void updateWhiteGame(JoinGameRequest game, String username) throws DataAccessException {
    try (var conn=DatabaseManager.getConnection()) {
      var statement="UPDATE games SET whiteUsername=? WHERE gameID=?";
      try (var ps=conn.prepareStatement(statement)) {
        ps.setString(1, username);
        ps.setInt(2, game.gameID());
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }


  public void deleteAll() throws DataAccessException {
    var statement = "TRUNCATE games";
    executeUpdate(statement);
  }

  private int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
    }
  }
}
