package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
  final private HashMap<String, AuthData> auths =new HashMap<>();

  public AuthData create(AuthData auth) {
    auth =new AuthData(auth.getAuthToken(), auth.getUsername());

    auths.put(auth.getAuthToken(), auth);
    return auth;
  }

  public AuthData getAuth(String username) {
    return auths.get(username);
  }

  public void deleteAuth(String username) {
    auths.remove(username);
  }

  public void deleteAll() {
    auths.clear();
  }
}

