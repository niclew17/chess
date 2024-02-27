package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.Message;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import services.Game;
import services.User;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class CreateGameHandler {
  private final Game service;

  public CreateGameHandler(MemoryGameDAO gameDAO, MemoryAuthDAO authDAO) {
    service= new Game(gameDAO, authDAO);

  }
  public Object creategame(Request req, Response res){
    var user= new Gson().fromJson(req.body(), CreateGameRequest.class);
    var newauth =req.headers("authorization");
    if (user.gameName().isEmpty()) {
      res.status(400);
      Message message=new Message("Error: bad request");
      res.body(new Gson().toJson(message));
    }
    else {
      try {
        CreateGameResponse game=service.createGame(newauth, user);
        res.status(200);
        return new Gson().toJson(game);
      }
      catch (DataAccessException e) {
       if (e.getMessage() == "Error: bad request") {
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
