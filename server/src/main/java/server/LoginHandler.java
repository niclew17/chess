package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.Message;
import request.LoginRequest;
import services.User;
import spark.Request;
import spark.Response;

public class LoginHandler {
  private final User service;
  public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {
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
      if (e.getMessage().equals("Error: unauthorized")) {
        res.status(401);
        Message message = new Message(e.getMessage());
        res.body(new Gson().toJson(message));
        return res.body();
      }
      else {
        res.status(500);
        Message message = new Message(e.getMessage());
        res.body(new Gson().toJson(message));
        return res.body();
      }
    }
  }
}
