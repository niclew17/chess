package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
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

  public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
    service=new User(userDAO, authDAO);
  }

  public Object register(Request req, Response res) {
    var user=new Gson().fromJson(req.body(), RegisterRequest.class);
    if (user.username()==null || user.password()==null || user.email()==null) {
      res.status(400);
      Message message=new Message("Error: bad request");
      res.body(new Gson().toJson(message));
      return res.body();
    }
    else {
      try {
        AuthData auth=service.register(user);
        res.status(200);
        return new Gson().toJson(auth);
      } catch (DataAccessException e) {
        if (e.getMessage().equals("Error: already taken")) {
          res.status(403);
        } else if (e.getMessage().equals("Error: bad request")) {
          res.status(401);
        } else {
          res.status(500);
        }
        Message message=new Message(e.getMessage());
        res.body(new Gson().toJson(message));
        return res.body();
      }
    }
  }
}
