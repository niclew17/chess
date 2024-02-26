package dataAccess;

import model.UserData;

public interface UserDAO {

  UserData createUser(UserData object) throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;

  void deleteAll() throws DataAccessException;
}