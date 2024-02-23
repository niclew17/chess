package dataAccess;

import model.UserData;

public interface UserDAO {

  UserData createUser(UserData object);

  UserData getUser(String username);

  void deleteAll();
}