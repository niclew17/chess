package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.Message;
import request.CreateGameRequest;
import response.CreateGameResponse;
import services.Game;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends HandlerErrorMethods{
  private final Game service;

  public CreateGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
    service= new Game(gameDAO, authDAO);
  }
  public Object creategame(Request req, Response res){
    var user= new Gson().fromJson(req.body(), CreateGameRequest.class);
    var newauth =req.headers("authorization");
    if (user.gameName()==null) {
      res.status(400);
      Message message=new Message("Error: bad request");
      res.body(new Gson().toJson(message));
      return res.body();
    }
    else {
      try {
        CreateGameResponse game=service.createGame(newauth, user);
        res.status(200);
        return new Gson().toJson(game);
      }
      catch (DataAccessException e) {
        sendError(res, e);
        Message message=new Message(e.getMessage());
        res.body(new Gson().toJson(message));
        return res.body();
      }
    }
  }
}
