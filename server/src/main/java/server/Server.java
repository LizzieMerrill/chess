package server;

import com.google.gson.Gson;
import java.util.Objects;
import java.util.UUID;
import handlers.*;
import service.DataService;
import service.GameService;
import service.UserService;
import spark.Spark;
import dataAccess.dao.*;

public class Server {

    protected final Gson gson = new Gson();
    protected final UserDAO userDAO = new MemoryUserDAO();
    protected final GameDAO gameDAO = new MemoryGameDAO();
    protected final AuthDAO authDAO = new MemoryAuthDAO();
    protected final DataService dataService = new DataService();
    protected final UserService userService = new UserService(gson, userDAO);
    protected final GameService gameService = new GameService();

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

    protected String generateUniqueAuthToken() {
        return UUID.randomUUID().toString();
    }

    private void registerEndpoints() {
        Spark.delete("/db", new ClearHandler());
//        Spark.delete("/db", (request, response) -> {
////            try {
////                gameDAO.clearChessData();
////                return gson.toJson(new StandardResponse(200, "Chess data cleared successfully"));
////            } catch (Exception e) {
////                response.status(500);
////                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
////            }
//        });

        //logout
        Spark.delete("/session", new LogoutHandler());

        //register
        Spark.post("/user", new RegisterHandler(gson, userService));

        //login
        Spark.post("/session", new LoginHandler());

        //create game
        Spark.post("/game", new CreateGameHandler());

        //join game
        Spark.put("/game", new JoinGameHandler());

        //list game
        Spark.get("/game", new ListGamesHandler());

    }

    protected boolean isValidTeamColor(String teamColor) {
        return "WHITE".equalsIgnoreCase(teamColor) || "BLACK".equalsIgnoreCase(teamColor);
    }

    // Helper method to check if a string is null or empty
    protected boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().isEmpty());
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

    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }
}
