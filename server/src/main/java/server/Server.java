package server;

import io.javalin.*;
import server.handler.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        registerRoutes();
    }

    private void registerRoutes() {
        javalin.post("/user", new RegisterHandler());
        javalin.post("/session", new LoginHandler());
        javalin.delete("/session", new LogoutHandler());
        javalin.get("/game", new ListHandler());
        javalin.post("/game", new CreateGameHandler());
        javalin.put("/game", new JoinGameHandler());
        javalin.delete("/db", new ClearHandler());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
