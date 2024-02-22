package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDAO implements UserDAOInterface {
  private int nextId=1;
  final private HashMap<Integer, UserData> users=new HashMap<>();

  public UserData add(UserData user) {
    user=new UserData(nextId++, user.getUsername(), user.getPassword(), user.getEmail());

    users.put(user.getId(), user);
    return user;
  }

  public HashMap<Integer, UserData> list() {
    return users;
  }

  public UserData get(int id) {
    return users.get(id);
  }

  public void delete(int id) {
    users.remove(id);
  }

  public void deleteAll() {
    users.clear();
  }
}