package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import dataAccess.data.AuthData;
import dataAccess.data.GameData;
import dataAccess.data.UserData;
import spark.Spark;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Server {

    private final Gson gson = new Gson();
    private final UserDAO userDAO = new MemoryUserDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

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

    private String generateUniqueAuthToken() {
        return UUID.randomUUID().toString();
    }

    private void registerEndpoints() {
        Spark.delete("/db", (request, response) -> {
            try {
                gameDAO.clearChessData();
                return gson.toJson(new StandardResponse(200, "Chess data cleared successfully"));
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });

        Spark.delete("/session", (request, response) -> {
            try {
                String authToken = request.headers("Authorization");
                if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                    response.status(401);
                    return gson.toJson(new StandardResponse(401, "Error: unauthorized"));
                }

                authDAO.removeAuthData(authToken);
                return gson.toJson(new StandardResponse(200, "Logout successful"));
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });

        Spark.post("/user", (request, response) -> {
            try {
                UserData userData = gson.fromJson(request.body(), UserData.class);

                // Check for bad request
                if (isNullOrEmpty(userData.getUsername()) || isNullOrEmpty(userData.getPassword()) || isNullOrEmpty(userData.getEmail())) {
                    response.status(400);
                    return gson.toJson(new StandardResponse(400, "Error: Bad request"));
                }

                // Check if the user already exists
                UserData existingUser = userDAO.getUser(userData.getUsername());
                if (existingUser != null) {
                    response.status(403);
                    return gson.toJson(new StandardResponse(403, "Error: User already exists"));
                }

                // Add the user using the UserDAO
                userDAO.addUser(userData);
                return gson.toJson(new StandardResponse(200, "User created successfully"));
            } catch (DataAccessException e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });

        Spark.post("/session", (request, response) -> {
            try {
                UserData userData = gson.fromJson(request.body(), UserData.class);

                System.out.println("Received login request for user: " + userData.getUsername());

                // Authenticate the user
                UserData storedUserData = userDAO.getUser(userData.getUsername());
                if (storedUserData != null && storedUserData.getPassword().equals(userData.getPassword())) {
                    AuthData existingAuthData = authDAO.getByUsername(userData.getUsername());

                    if (existingAuthData != null) {
                        return gson.toJson(existingAuthData);
                    } else {
                        AuthData authData = new AuthData(userData);
                        String newAuthToken = generateUniqueAuthToken();
                        authData.setAuthToken(newAuthToken);
                        authDAO.addAuthToken(authData);
                        return gson.toJson(authData);
                    }
                } else {
                    response.status(401);
                    return gson.toJson(new StandardResponse(401, "{ \"message\": \"Error: unauthorized\" }"));
                }
            } catch (DataAccessException e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }"));
            }
        });

        Spark.post("/game", (request, response) -> {
            try {
                // Check if the request contains a valid Authorization header
                String authToken = request.headers("Authorization");
                if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                    response.status(401); // Unauthorized
                    return gson.toJson(new StandardResponse(401, "Error: Unauthorized"));
                }

                // Your implementation for creating a game
                // Example: gameDAO.createGame(request.body());
                // Adapt based on how your client sends game data in the request.

                // For simplicity, let's assume createGame returns the created game ID
                int gameId = gameDAO.createGame(request.body());

                return gson.toJson(new StandardResponse(200, "Game created successfully. Game ID: " + gameId));
            } catch (DataAccessException e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            } catch (Exception e) {
                response.status(403); // Forbidden
                return gson.toJson(new StandardResponse(403, "Error: Invalid team color"));
            }
        });



        Spark.put("/game", (request, response) -> {
            try {
                // Check if the request contains a valid Authorization header
                String authToken = request.headers("Authorization");
                if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                    response.status(401); // Unauthorized
                    return gson.toJson(new StandardResponse(401, "Error: Unauthorized"));
                }
                // Check if the game ID in the request is valid (e.g., not null or empty)
                String gameId = request.queryParams("gameId");
                if (isNullOrEmpty(gameId)) {
                    response.status(400); // Bad Request
                    return gson.toJson(new StandardResponse(400, "Error: Bad Request - Invalid Game ID"));
                }

//
//                // Check if the user is authorized to watch the game
//                if (!gameDAO.isPlayerInGame(authToken, gameId)) {
//                    response.status(401); // Unauthorized
//                    return gson.toJson(new StandardResponse(401, "Error: Unauthorized - You are not authorized to watch this game"));
//                }

                // Your implementation for updating a game
                // Example: gameDAO.updateGame(request.body());
                // Adapt based on how your client sends game data in the request.

                // Return a success message as JSON
                response.type("application/json");
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Game updated successfully");
                return gson.toJson(jsonResponse);
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });





        Spark.get("/game", (request, response) -> {
            try {
                // Your implementation for handling the GET request for /game

                // Get the list of games
                JsonArray gamesList = gameDAO.getAllGames();

                // Check if there are no games
                if (gamesList == null || gamesList.isEmpty()) {
                    response.type("application/json");
                    return "{ \"noGames\": true }";
                }

                // Check if there are multiple games
                response.type("application/json");
                return gson.toJson(new StandardResponse(200, gamesList.toString()));
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });




        // Register other endpoints similarly
        // ...
    }

    // Helper method to check if a string is null or empty
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
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
        server.run(4567);
    }
}
