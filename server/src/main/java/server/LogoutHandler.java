package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.Message;
import services.User;
import spark.Request;
import spark.Response;

public class LogoutHandler extends HandlerErrorMethods{
  private final User service;

  public LogoutHandler(UserDAO userDAO, AuthDAO authDAO) {
    service=new User(userDAO, authDAO);
  }

  public Object logout(Request req, Response res) {
    var user=req.headers("authorization");
    try {
      service.logout(user);
      res.status(200);
      return "{}";
    }
    catch (DataAccessException e) {
      sendError(res, e);
      return res.body();
    }
  }
}
