package dataAccess;

import model.UserData;

import java.util.HashMap;

public interface UserDAOInterface {

  Object add(UserData object);

  HashMap<Integer, UserData> list();

  UserData get(int id);

  void delete(int id);

  void deleteAll();
}