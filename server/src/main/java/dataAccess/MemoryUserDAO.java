package dataAccess;

import model.UserData;
import request.RegisterRequest;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
  final private static HashMap<String, UserData> users =new HashMap<>();

  public UserData createUser(RegisterRequest user) {
    UserData newuser=new UserData(user.username(), user.password(), user.email());
    users.put(user.username(), newuser);
    return newuser;
  }

  @Override
  public UserData getUser(String username) {
    return users.get(username);
  }


  public void deleteAll() {
    users.clear();
  }
}