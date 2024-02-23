package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
  final private HashMap<String, UserData> users =new HashMap<>();

  public UserData createUser(UserData user) {
    user=new UserData(user.getUsername(), user.getPassword(), user.getEmail());

    users.put(user.getUsername(), user);
    return user;
  }

  @Override
  public UserData getUser(String username) {
    return users.get(username);
  }

  public void delete(String username) {
    users.remove(username);
  }

  public void deleteAll() {
    users.clear();
  }
}