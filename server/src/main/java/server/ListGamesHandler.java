package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.Message;
import model.UserData;
import request.CreateGameRequest;
import response.ListGamesResponse;
import services.Game;
import services.User;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGamesHandler {
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
      if (e.getMessage().equals("Error: unauthorized")) {
        res.status(401);
      }
      else {
        res.status(500);
      }
      Message message=new Message(e.getMessage());
      res.body(new Gson().toJson(message));
      return res.body();
    }
  }
}
