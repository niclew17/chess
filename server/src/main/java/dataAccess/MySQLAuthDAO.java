package dataAccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO{
  public MySQLAuthDAO() throws DataAccessException {
    configureDatabase();
  }
  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  auth (
              `authtoken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authtoken`)
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
      throw new DataAccessException(String.format("Unable to configure Auth database: %s", ex.getMessage()));
    }
  }

  public AuthData createAuth(String username) throws DataAccessException {
    String authtoken = UUID.randomUUID().toString();
    var statement = "INSERT INTO auth (authtoken, username) VALUES (?, ?)";
    executeUpdate(statement, authtoken, username);
    return new AuthData(authtoken,username);
  }

  public AuthData getUser(String authtoken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authtoken, username FROM auth WHERE authtoken=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, authtoken);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readAuth(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }
  private AuthData readAuth(ResultSet rs) throws SQLException {
    var authtoken= rs.getString("authtoken");
    var username = rs.getString("username");
    return new AuthData(authtoken,username);

  }

  public void deleteAuth(String authtoken) throws DataAccessException{
    var statement = "DELETE FROM auth WHERE authtoken=?";
    try {
      executeUpdate(statement, authtoken);
    }
    catch (Exception e){
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }


  public void deleteAll() throws DataAccessException{
      var statement = "TRUNCATE auth";
      executeUpdate(statement);

  }

  private void executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
    }
  }

}

