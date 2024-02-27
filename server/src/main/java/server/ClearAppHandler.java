package server;
import com.google.gson.Gson;
import dataAccess.*;
import model.Message;
import services.DB;

import org.eclipse.jetty.client.ResponseNotifier;
import spark.Request;
import spark.Response;


public class ClearAppHandler {
  private final DB service;
  public ClearAppHandler(MemoryUserDAO userDAO, MemoryAuthDAO authDAO, MemoryGameDAO gameDAO){
   service = new DB(userDAO, authDAO, gameDAO);
  }
  public Object clear(Request req, Response res){
    if(req.body() != null){
    service.clear();
    res.status(200);
    return "";
  }
    else{
      res.status(500);
      Message message = new Message("Error: description");
      res.body(new Gson().toJson(message));
      return "";
    }
  }
}
