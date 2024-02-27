package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.dao.UserDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.MemoryUserDAO;
import dataAccess.dao.MemoryGameDAO;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.MemoryAuthDAO;
import dataAccess.data.UserData;
import spark.*;

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

    private void registerEndpoints() {
        Spark.delete("/db", (request, response) -> {
            try {
                // Your implementation to clear the chess-related data
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

        Spark.post("/user", (request, response) -> {
            try {
                // Convert the JSON string to a UserData object
                UserData userData = gson.fromJson(request.body(), UserData.class);

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
                // Convert the JSON string to a UserData object
                UserData userData = gson.fromJson(request.body(), UserData.class);

                // Assuming userData contains username and password
                if (authDAO.authenticateUser(userData)) {
                    // Successfully authenticated
                    return gson.toJson(new StandardResponse(200, "Login successful"));
                } else {
                    // Authentication failed
                    response.status(401); // Unauthorized
                    return gson.toJson(new StandardResponse(401, "Login failed"));
                }
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });

        // Register other endpoints similarly
        // ...
    }//end register endpoints

    public static void main(String[] args) {
        Server server = new Server();
        server.run(4567);
    }

}