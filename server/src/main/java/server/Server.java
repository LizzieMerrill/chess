package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.*;

public class Server {

    private final Gson gson = new Gson();  // Gson instance for serialization/deserialization

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        registerEndpoints();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void registerEndpoints() {
        // Example: Registering the 'clear' endpoint
        Spark.delete("/db", (request, response) -> {
            try {
                // Your implementation to clear the database
                // Example: clearDatabase();
                return gson.toJson(new StandardResponse(200, "Database cleared successfully"));
            } catch (DataAccessException e) {
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        });

        // Register other endpoints similarly
        // ...
    }
}
