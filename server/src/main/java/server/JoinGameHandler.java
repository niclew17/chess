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
    if (user.gameID() < 0) {
      res.status(400);
      Message message=new Message("Error: bad request");
      res.body(new Gson().toJson(message));
      return res.body();
    }
    else {
      try {
        service.joinGame(newauth, user);
        res.status(200);
        return "{}";
      }
      catch (DataAccessException e) {
        if (e.getMessage().equals("Error: already taken")) {
          res.status(403);
        }
        else if(e.getMessage().equals("Error: unauthorized")) {
          res.status(401);
        }
        else if(e.getMessage().equals("Error: bad request")) {
          res.status(400);
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
}
