package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.Message;
import model.UserData;
import request.LoginRequest;
import services.User;
import spark.Request;
import spark.Response;

public class LoginHandler {
  private final User service;
  public LoginHandler(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
    service= new User(userDAO, authDAO);
  }
  public Object login(Request req, Response res){
    var user= new Gson().fromJson(req.body(), LoginRequest.class);
    try {
      AuthData data = service.login(user);
      res.status(200);
      return new Gson().toJson(data);
    }
    catch (DataAccessException e) {
      if (e.getMessage() == "Error: Unauthorized") {
        res.status(401);
        Message message = new Message(e.getMessage());
        res.body(new Gson().toJson(message));
      }
      else {
        res.status(500);
        Message message = new Message(e.getMessage());
        res.body(new Gson().toJson(message));
      }
    }
    return "";
  }
}
