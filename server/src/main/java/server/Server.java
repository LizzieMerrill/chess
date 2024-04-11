package server;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.Objects;
import dataAccess.access.DataAccessException;
import exception.ResponseException;
import handlers.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import server.WebSocket.WSocketHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import dataAccess.dao.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

public class Server {

    final Gson gson = new Gson();
    final UserDAO userDAO = new SQLUserDAO();
    final GameDAO gameDAO = new SQLGameDAO();
    final AuthDAO authDAO = new SQLAuthDAO();
    private final WSocketHandler wSocketHandler = new WSocketHandler();


    public Server() {
    }

//    public int run(int desiredPort) {
//        Spark.port(desiredPort);
//        Spark.staticFiles.location("web");
//        registerWebSocketHandlers();
//        registerEndpoints();
//        Spark.awaitInitialization();
//        return Spark.port();
//    }
    public int run(int port) {
        Spark.port(port);

        Spark.staticFiles.location("public");

        Spark.webSocket("/connect", wSocketHandler);

//        Spark.post("/pet", this::addPet);
//        Spark.get("/pet", this::listPets);
//        Spark.delete("/pet/:id", this::deletePet);
//        Spark.delete("/pet", this::deleteAllPets);
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
        res.status(ex.StatusCode());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void registerWebSocketHandlers() {
        Spark.webSocket("/connect", new WSocketHandler());
    }


    private void registerEndpoints() {
        //clear
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
