package server;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.*;

import static org.slf4j.MDC.clear;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req,res) -> new ClearAppHandler().clear(req,res));
        Spark.post("/user", (req,res) -> new RegisterHandler().register(req,res));
        Spark.post("/session", (req,res) -> new LoginHandler().login(req,res));
        Spark.delete("/session", (req,res) -> new LogoutHandler().logout(req,res));
        Spark.get("/game", (req,res) -> new ListGamesHandler().listgames(req,res));
        Spark.post("/game", (req,res) -> new CreateGameHandler().creategame(req,res));
        Spark.put("/game", (req,res) -> new JoinGameHandler().joingame(req,res));
        Spark.init();


        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
