package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.LoginRequest;
import request.RegisterRequest;

public class User {
  private final UserDAO userDAO;
  private final AuthDAO authDAO;
  public User(UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;

  }

  public AuthData register(RegisterRequest user) throws DataAccessException{
    if(userDAO.getUser(user.username()) != null){
      throw new DataAccessException("Error: already taken");
    }
    else{
      userDAO.createUser(user);
      return authDAO.createAuth(user.username());
    }
  }
  public AuthData login(LoginRequest user) throws DataAccessException{
    AuthData data;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    if(userDAO.getUser(user.username()) != null && encoder.matches(user.password(), userDAO.getUser(user.username()).getPassword())){
      data = authDAO.createAuth(user.username());
    }
    else{
      throw new DataAccessException("Error: unauthorized");
    }
    return data;
  }

  public void isAuthorized(String authtoken) throws DataAccessException {
    if(authDAO.getUser(authtoken) == null){
      throw new DataAccessException("Error: unauthorized");
    }
  }
  public void logout(String authtoken) throws DataAccessException{
      isAuthorized(authtoken);
      authDAO.deleteAuth(authtoken);
    }
  }


