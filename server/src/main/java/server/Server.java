package server;

import server.handler.*;
import server.websocket.WebSocketHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", new WebSocketHandler());

        // Register your endpoints and handle exceptions here
        //Spark.<HTTP method>("<URL PATH>", new <Appropiate Handler Class> (implementation of Route);
        // .handle method Invoked when a request is made on this route's corresponding path
        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.get("/game", new ListHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());
        Spark.delete("/db", new ClearHandler());

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
