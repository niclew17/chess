package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
  final private HashMap<String, AuthData> auths =new HashMap<>();

  public AuthData createAuth(String username) {
    String authtoken = UUID.randomUUID().toString();
    AuthData auth =new AuthData(authtoken,username);
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

