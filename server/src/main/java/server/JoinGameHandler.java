package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import model.Message;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import services.Game;
import services.User;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class JoinGameHandler {
  private final Game service;
  public JoinGameHandler(MemoryGameDAO gameDAO, MemoryAuthDAO authDAO) {
    service= new Game(gameDAO, authDAO);

  }
  public Object joingame(Request req, Response res){
    var user=new Gson().fromJson(req.body(), JoinGameRequest.class);
    var newauth=req.headers("authorization");
    if (user.playerColor().isEmpty() || user.gameID() < 0) {
      res.status(400);
      Message message=new Message("Error: bad request");
      res.body(new Gson().toJson(message));
    }
    else {
      try {
        service.joinGame(newauth, user);
        res.status(200);
      }
      catch (DataAccessException e) {
        if (e.getMessage() == "Error: already taken") {
          res.status(403);
          Message message=new Message(e.getMessage());
          res.body(new Gson().toJson(message));
        }
        else if(e.getMessage() == "Error: bad request") {
          res.status(401);
          Message message=new Message(e.getMessage());
          res.body(new Gson().toJson(message));
        }
        else {
          res.status(500);
          Message message=new Message(e.getMessage());
          res.body(new Gson().toJson(message));
        }
      }
    }
    return "";
  }

}
