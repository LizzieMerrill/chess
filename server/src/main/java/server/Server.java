package server;

import com.google.gson.Gson;
import java.util.Objects;
import handlers.*;
import spark.Spark;
import dataAccess.dao.*;

public class Server {

    final Gson gson = new Gson();
    final UserDAO userDAO = new SQLUserDAO();
    final GameDAO gameDAO = new SQLGameDAO();

    final AuthDAO authDAO = new SQLAuthDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        registerEndpoints();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
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
