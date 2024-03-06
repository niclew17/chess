package server;

import dataAccess.*;
import spark.*;

import static org.slf4j.MDC.clear;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO =null;
        try {
            authDAO=new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        GameDAO gameDAO = new MemoryGameDAO();
        // Register your endpoints and handle exceptions here.
        AuthDAO finalAuthDAO=authDAO;
        Spark.delete("/db", (req, res) -> new ClearAppHandler(userDAO, finalAuthDAO, gameDAO).clear(req,res));
        Spark.post("/user", (req, res) -> new RegisterHandler(userDAO, finalAuthDAO).register(req,res));
//        Spark.post("/session", (req,res) -> new LoginHandler(userDAO, authDAO).login(req,res));
//        Spark.delete("/session", (req,res) -> new LogoutHandler(userDAO, authDAO).logout(req,res));
//        Spark.get("/game", (req,res) -> new ListGamesHandler(gameDAO, authDAO).listgames(req,res));
//        Spark.post("/game", (req,res) -> new CreateGameHandler(gameDAO, authDAO).creategame(req,res));
//        Spark.put("/game", (req,res) -> new JoinGameHandler(gameDAO, authDAO).joingame(req,res));
        Spark.init();


        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
