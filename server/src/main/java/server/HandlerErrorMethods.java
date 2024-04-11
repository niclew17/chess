package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.Message;
import spark.Response;

public class HandlerErrorMethods {
  public static void sendError(Response res, DataAccessException e) {
    if (e.getMessage().equals("Error: unauthorized")) {
      res.status(401);
    }
    else {
      res.status(500);
    }
    Message message=new Message(e.getMessage());
    res.body(new Gson().toJson(message));
  }
}
