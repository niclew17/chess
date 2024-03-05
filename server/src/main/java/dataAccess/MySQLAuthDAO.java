package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MySQLAuthDAO implements AuthDAO{
  final private static HashMap<String, AuthData> auths =new HashMap<>();

  public AuthData createAuth(String username) {
    String authtoken = UUID.randomUUID().toString();
    AuthData auth =new AuthData(authtoken,username);
    auths.put(auth.getAuthToken(), auth);
    return auth;
  }

  public AuthData getUser(String authtoken) {
    return auths.get(authtoken);
  }

  public void deleteAuth(String authtoken) {
    auths.remove(authtoken);
  }

  public void deleteAll() {
    auths.clear();
  }
}

