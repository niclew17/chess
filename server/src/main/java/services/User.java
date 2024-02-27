package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;

public class User {
  private final MemoryUserDAO userDAO;
  private final MemoryAuthDAO authDAO;
  public User(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;

  }

  public AuthData register(RegisterRequest user) throws DataAccessException{
    if(userDAO.getUser(user.username()) != null){
      throw new DataAccessException("Error: already taken", 403);
    }
    else{
      UserData data = userDAO.createUser(user);
      return authDAO.createAuth(user.username());
    }
  }
  public AuthData login(LoginRequest user) throws DataAccessException{
    AuthData data;
    if(userDAO.getUser(user.username()) != null && userDAO.getUser(user.username()).getPassword().equals(user.password())){
      data = authDAO.createAuth(user.username());
    }
    else{
      throw new DataAccessException("Error: unauthorized",401);
    }
    return data;
  }

  public void isAuthorized(String authtoken) throws DataAccessException {
    if(authDAO.getUser(authtoken) == null){
      throw new DataAccessException("Error: unauthorized", 401);
    }
  }
  public void logout(String authtoken) throws DataAccessException{
      isAuthorized(authtoken);
      authDAO.deleteAuth(authtoken);
    }
  }


