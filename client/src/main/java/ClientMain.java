import dataAccess.DataAccessException;

public class ClientMain {
  public static void main(String[] args) throws DataAccessException {
    var serverUrl = "http://localhost:8080";
    if (args.length == 1) {
      serverUrl = args[0];
    }

    new Repl(serverUrl).run();
  }

}