package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import dataAccess.data.AuthData;
import dataAccess.data.GameData;
import dataAccess.data.UserData;
import spark.Spark;

import java.util.Objects;
import java.util.UUID;

public class Server {

    private final Gson gson = new Gson();
    private final UserDAO userDAO = new MemoryUserDAO();  // Use MemoryUserDAO for in-memory storage
    private final GameDAO gameDAO = new MemoryGameDAO();  // Use MemoryGameDAO for in-memory storage
    private final AuthDAO authDAO = new MemoryAuthDAO();  // Use MemoryAuthDAO for in-memory storage

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
                // Explicitly throw a DataAccessException to be caught by the catch block
                throw new DataAccessException("Error clearing chess data");
            }
//            } catch (DataAccessException e) {
//                response.status(500);
//                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//            }
        });
        Spark.delete("/session", (request, response) -> {
            try {
                String authToken = request.headers("Authorization");
                if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                    response.status(401); // Unauthorized
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
                // Convert the JSON string to a UserData object
                UserData userData = gson.fromJson(request.body(), UserData.class);

                // Add the user using the UserDAO
                userDAO.addUser(userData);

                return gson.toJson(new StandardResponse(200, "User created successfully"));
            } catch (Exception e) {
                response.status(401);
                return gson.toJson(new StandardResponse(401, "Error: " + e.getMessage()));
            }
        });

        Spark.post("/session", (request, response) -> {
            try {
                UserData userData = gson.fromJson(request.body(), UserData.class);

                System.out.println("Received login request for user: " + userData.getUsername());

                // Authenticate the user
                if (userData.getUsername().equals(userDAO.getUser(userData.getUsername()).getUsername()) &&
                        userData.getUsername() != null &&
                        userData.getPassword().equals(userDAO.getUser(userData.getUsername()).getPassword()) &&
                        userData.getPassword() != null) {

                    // Check if user already has an existing auth token
                    AuthData existingAuthData = authDAO.getByUsername(userData.getUsername());

                    if (existingAuthData != null) {
                        // User already has an auth token, return it
                        return gson.toJson(existingAuthData);
                    } else {
                        // Generate a new auth token for the user
                        AuthData authData = new AuthData(userData);
                        String newAuthToken = generateUniqueAuthToken(); // Implement a method to generate a unique token

                        // Set the new auth token for the user
                        authData.setAuthToken(newAuthToken);
                        authDAO.addAuthToken(authData);

                        // Return the user data with the new auth token
                        return gson.toJson(authData);
                    }
                } else {
                    // Authentication failed
                    response.status(401); // Unauthorized
                    return gson.toJson(new StandardResponse(401, "{ \"message\": \"Error: unauthorized\" }"));
                }
            } catch (Exception e) {
                response.status(401);
                return gson.toJson(new StandardResponse(401, "{ \"message\": \"Error: unauthorized\" }"));
            }
        });



        Spark.post("/game", (request, response) -> {
            try {
                // Assuming you have a method in your GameDAO to create a game
                // Example: gameDAO.createGame(request.body());
                // You should adapt this based on how your client sends game data in the request.

                // For simplicity, let's assume that createGame returns the created game ID
                int gameId = gameDAO.createGame(request.body());

                return gson.toJson(new StandardResponse(200, "Game created successfully. Game ID: " + gameId));
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });

        Spark.put("/game", (request, response) -> {
            try {
                // Extract necessary information from the request
                String gameData = request.body(); // Assuming the game data is sent in the request body

                // Update game data using your GameDAO
                gameDAO.updateGame(new GameData(gameData)); // Assuming you have a GameData class

                // Return a success message as JSON
                response.type("application/json");
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Game updated successfully");
                return new Gson().toJson(jsonResponse);
            } catch (Exception e) {
                // Handle exceptions and return an appropriate response
                response.status(500);
                return "Error updating game: " + e.getMessage();
            }
        });

        Spark.get("/game", (request, response) -> {
            try {
                // Your implementation for handling the GET request for /game
                // Retrieve necessary information from the request, fetch game data from GameDAO, etc.

                // For example, you can return the list of games as JSON
                response.type("application/json");
                return "{ \"games\": " + gameDAO.getAllGames() + "}"; // Assuming getAllGames now returns a String directly
            } catch (Exception e) {
                // Handle exceptions and return an appropriate response
                response.status(500);
                return "Error retrieving games: " + e.getMessage();
            }
        });



        // Register other endpoints similarly
        // ...
    }//end register endpoints

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

        GameDAO gameDAO = new MemoryGameDAO();


    }

}