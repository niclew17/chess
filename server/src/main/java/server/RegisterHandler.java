package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.Message;
import model.UserData;
import request.RegisterRequest;
import services.DB;
import services.User;
import spark.Request;
import spark.Response;

public class RegisterHandler {
  private final User service;

  public RegisterHandler(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
    service=new User(userDAO, authDAO);
  }

  public Object register(Request req, Response res) {
    var user=new Gson().fromJson(req.body(), RegisterRequest.class);
    if (user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()) {
      res.status(400);
      Message message=new Message("Error: bad request");
      res.body(new Gson().toJson(message));
    }
    else {
      try {
        AuthData auth=service.register(user);
        res.status(200);
        return new Gson().toJson(auth);
      } catch (DataAccessException e) {
        if (e.getMessage() == "Error: already taken") {
          res.status(403);
          Message message=new Message(e.getMessage());
          res.body(new Gson().toJson(message));
        } else if (e.getMessage() == "Error: bad request") {
          res.status(401);
          Message message=new Message(e.getMessage());
          res.body(new Gson().toJson(message));
        } else {
          res.status(500);
          Message message=new Message(e.getMessage());
          res.body(new Gson().toJson(message));
        }
      }
    }
      return "";
    }
}
