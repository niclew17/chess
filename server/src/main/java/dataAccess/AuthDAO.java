package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

  AuthData createAuth(String username) throws DataAccessException;

  AuthData getAuth(String auth) throws DataAccessException;

  void deleteAuth(String auth) throws DataAccessException;
  void deleteAll() throws DataAccessException;
}
