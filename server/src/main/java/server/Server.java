package server;

import com.google.gson.Gson;
import java.util.Objects;
import exception.ResponseException;
import handlers.*;
import server.WebSocket.WSocketHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import dataAccess.dao.*;

public class Server {

    final Gson gson = new Gson();
    final UserDAO userDAO = new SQLUserDAO();
    final GameDAO gameDAO = new SQLGameDAO();
    final AuthDAO authDAO = new SQLAuthDAO();
    private final WSocketHandler wSocketHandler = new WSocketHandler();


    public Server() {
    }

    public int run(int port) {
        Spark.port(port);

        Spark.staticFiles.location("public");

        Spark.webSocket("/connect", wSocketHandler);

        Spark.delete("/db", new ClearHandler(authDAO, userDAO, gameDAO));

        //logout
        Spark.delete("/session", new LogoutHandler(authDAO));

        //register
        Spark.post("/user", new RegisterHandler(gson, userDAO, authDAO));

        //login
        Spark.post("/session", new LoginHandler(userDAO, authDAO));

        //create game
        Spark.post("/game", new CreateGameHandler(authDAO, gameDAO, userDAO));

        //join game
        Spark.put("/game", new JoinGameHandler(authDAO, gameDAO, userDAO));

        //list game
        Spark.get("/game", new ListGamesHandler(authDAO, gameDAO, userDAO));
        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(gson, server.gson) && Objects.equals(userDAO, server.userDAO) && Objects.equals(gameDAO, server.gameDAO) && Objects.equals(authDAO, server.authDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gson, userDAO, gameDAO, authDAO);
    }

    @Override
    public String toString() {
        return "Server{" +
                "gson=" + gson +
                ", userDAO=" + userDAO +
                ", gameDAO=" + gameDAO +
                ", authDAO=" + authDAO +
                '}';
    }
}
