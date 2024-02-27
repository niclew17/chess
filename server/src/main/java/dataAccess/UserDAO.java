package dataAccess;

import model.UserData;
import request.RegisterRequest;

public interface UserDAO {

  UserData createUser(RegisterRequest object) throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;

  void deleteAll() throws DataAccessException;
}