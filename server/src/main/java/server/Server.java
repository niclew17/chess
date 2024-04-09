package server;

import Websocket.WebsocketHandler;
import dataAccess.*;
import spark.Spark;

public class Server {
    private WebsocketHandler webSocketHandler;

    public Server() {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

        UserDAO userDAO = null;
        GameDAO gameDAO = null;
        AuthDAO authDAO =null;
        try {
            gameDAO=new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            userDAO=new MySQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            authDAO=new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        // Register your endpoints and handle exceptions here.
        UserDAO finalUserDAO = userDAO;
        GameDAO finalGameDAO = gameDAO;
        AuthDAO finalAuthDAO=authDAO;
        webSocketHandler= new WebsocketHandler(finalGameDAO, finalAuthDAO);
        Spark.delete("/db", (req, res) -> new ClearAppHandler(finalUserDAO, finalAuthDAO, finalGameDAO).clear(req,res));
        Spark.post("/user", (req, res) -> new RegisterHandler(finalUserDAO, finalAuthDAO).register(req,res));
        Spark.post("/session", (req, res) -> new LoginHandler(finalUserDAO, finalAuthDAO).login(req,res));
        Spark.delete("/session", (req, res) -> new LogoutHandler(finalUserDAO, finalAuthDAO).logout(req,res));
        Spark.get("/game", (req, res) -> new ListGamesHandler(finalGameDAO, finalAuthDAO).listgames(req,res));
        Spark.post("/game", (req, res) -> new CreateGameHandler(finalGameDAO, finalAuthDAO).creategame(req,res));
        Spark.put("/game", (req,res) -> new JoinGameHandler(finalGameDAO, finalAuthDAO).joingame(req,res));
        Spark.init();


        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
