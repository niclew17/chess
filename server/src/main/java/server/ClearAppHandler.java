package server;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import services.DB;

import org.eclipse.jetty.client.ResponseNotifier;
import spark.Request;
import spark.Response;


public class ClearAppHandler {
  private final DB service;
  public ClearAppHandler(){
   service = new DB();
  }
  public Object clear(Request req, Response res){
    service.clear();
    res.status(200);
    return "";
  }
}
