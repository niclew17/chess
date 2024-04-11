package server;

import dataAccess.DataAccessException;
import spark.Response;

public class HandlerErrorMethods {
  public static void sendError(Response res, DataAccessException e) {
    if (e.getMessage().equals("Error: unauthorized")) {
      res.status(401);
    }
    else {
      res.status(500);
    }
  }
}
