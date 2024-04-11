package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.RegisterRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static java.sql.Types.NULL;

public class MySQLUserDAO implements UserDAO {
  public MySQLUserDAO() throws DataAccessException {
    configureDatabase();
  }
  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
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
      throw new DataAccessException(String.format("Unable to configure User database: %s", ex.getMessage()));
    }
  }
  final private static HashMap<String, UserData> users =new HashMap<>();

  public UserData createUser(RegisterRequest user) throws DataAccessException {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(user.password());
    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?,?)";
    executeUpdate(statement, user.username(), hashedPassword, user.email());
    return new UserData(user.username(), hashedPassword, user.email());
  }

  public UserData getUser(String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM user WHERE username=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, username);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readUser(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  private UserData readUser(ResultSet rs) throws SQLException {
    var username = rs.getString("username");
    var password= rs.getString("password");
    var email = rs.getString("email");
    return new UserData(username, password, email);
  }


  public void deleteAll() throws DataAccessException {
    var statement = "TRUNCATE user";
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