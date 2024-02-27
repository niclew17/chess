package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.Message;
import model.UserData;
import services.Game;
import services.User;
import spark.Request;
import spark.Response;

public class LogoutHandler {
  private final User service;

  public LogoutHandler(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
    service=new User(userDAO, authDAO);
  }

  public Object logout(Request req, Response res) {
    var user=req.headers("authorization");
    try {
      service.logout(user);
      res.status(200);
      return "";
    }
    catch (DataAccessException e) {
      if (e.getMessage().equals("Error: Unauthorized")) {
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
