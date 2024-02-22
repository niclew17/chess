package server;

import spark.*;

import static org.slf4j.MDC.clear;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req,res) -> clear(req,res));
        Spark.post("/user", (req,res) -> register(req,res));
        Spark.post("/session", (req,res) -> login(req,res));
        Spark.delete("/session", (req,res) -> logout(req,res));
        Spark.get("/game", (req,res) -> listgames(req,res));
        Spark.post("/game", (req,res) -> creategame(req,res));
        Spark.put("/game", (req,res) -> joingame(req,res));
        Spark.init();


        Spark.awaitInitialization();
        return Spark.port();
    }
    public void clear(Request req, Response res){
        services.User.clear();
    }
    public Object register(Request req, Response res){
        return null;
    }
    public Object login(Request req, Response res){
        return null;
    }
    public Object logout(Request req, Response res){
        return null;
    }
    public Object listgames(Request req, Response res){
        return null;
    }
    public Object creategame(Request req, Response res){
        return null;
    }
    public Object joingame(Request req, Response res){
        return null;
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
