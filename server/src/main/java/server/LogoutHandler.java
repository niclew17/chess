package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import services.User;
import spark.Request;
import spark.Response;

public class LogoutHandler {
  private final User service;
  public LogoutHandler() {
    service= new User();
  }
  public Object logout(Request req, Response res){
    var user= req.headers("authorization");
    boolean auth=service.logout(user);
    if (auth == true) {
      res.status(200);
    }
    else {
      res.status(401);
    }
    return null;
  }
}
