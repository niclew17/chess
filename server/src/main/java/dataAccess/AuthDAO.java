package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

  AuthData createAuth(String username) throws DataAccessException;

  AuthData getUser(String auth) throws DataAccessException;

  void deleteAuth(String auth) throws DataAccessException;
  void deleteAll() throws DataAccessException;
}
