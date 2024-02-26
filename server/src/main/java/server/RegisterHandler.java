package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import services.DB;
import services.User;
import spark.Request;
import spark.Response;

public class RegisterHandler {
  private final User service;

  public RegisterHandler() {
    service=new User();
  }

  public Object register(Request req, Response res) {
    var user=new Gson().fromJson(req.body(), UserData.class);
    AuthData auth=service.register(user);
    if (auth != null) {
      res.status(200);
      return new Gson().toJson(auth);
    }
    else {
      res.status(404);
      return "";
    }
  }
}
