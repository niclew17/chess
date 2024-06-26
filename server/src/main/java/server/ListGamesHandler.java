package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import response.ListGamesResponse;
import services.Game;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends HandlerErrorMethods{
  private final Game service;

  public ListGamesHandler(GameDAO gameDAO, AuthDAO authDAO) {
    service= new Game(gameDAO, authDAO);
  }
  public Object listgames(Request req, Response res) {
    var user=req.headers("authorization");
    try {
      ListGamesResponse gamedata=service.listGames(user);
      res.status(200);
      return new Gson().toJson(gamedata);
    }
    catch (DataAccessException e) {
      sendError(res, e);
      return res.body();
    }
  }
}
