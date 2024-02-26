package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import services.Game;
import services.User;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGamesHandler {
  private final Game service;
  private final User userservice;
  public ListGamesHandler() {
    service= new Game();
    userservice = new User();
  }
  public Object listgames(Request req, Response res){
    var user= req.headers("authorization");
    boolean auth=userservice.isAuthorized(user);
    Collection<GameData> gamedata = service.listGames();
    if (auth == true) {

      res.status(200);
    }
    else {
      res.status(401);
    }
    return null;
  }
}
