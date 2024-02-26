package services;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class User {
  private final MemoryUserDAO userDAO;
  private final MemoryAuthDAO authDAO;
  public User() {
    userDAO = new MemoryUserDAO();
    authDAO = new MemoryAuthDAO();
  }

  public AuthData register(UserData user) {
    AuthData data;
    if(userDAO.getUser(user.getUsername()) == null){
      userDAO.createUser(user);
      data = authDAO.createAuth(user.getUsername());
    }
    else{
      data = null;
    }
    return data;
  }
  public AuthData login(UserData user) {
    AuthData data;
    if(userDAO.getUser(user.getUsername()) != null){
      data = authDAO.createAuth(user.getUsername());
    }
    else{
      data = null;
    }
    return data;
  }

  public boolean isAuthorized(String authtoken){
    if(userDAO.getUser(authtoken) != null){
      return true;
    }
    else{
      return false;
    }
  }
  public boolean logout(String authtoken) {
    if(isAuthorized(authtoken)){
      userDAO.delete(authtoken);
      return true;
    }
    else{
      return false;
    }
  }

}
