package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

  AuthData create(AuthData auth);

  AuthData getAuth(String auth);

  void deleteAuth(String auth);
  void deleteAll();
}
