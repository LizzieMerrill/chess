//package handlers;
//
//import com.google.gson.JsonObject;
//import server.Server;
//import server.StandardResponse;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//public class JoinGameHandler extends Server implements Route {
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//        try {
//            // Check if the request contains a valid Authorization header
//            String authToken = request.headers("Authorization");
//            if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
//                response.status(401); // Unauthorized
//                return gson.toJson(new StandardResponse(401, "Error: Unauthorized"));
//            }
//
//            // Check if the game ID in the request is valid (e.g., not null or empty)
//            String gameId = request.queryParams("gameId");
//            if (isNullOrEmpty(gameId)) {
//                response.status(400); // Bad Request
//                return gson.toJson(new StandardResponse(400, "Error: Bad Request - Invalid Game ID"));
//            }
//
//            // Check if the user is authorized to update the game
//            if (!gameDAO.isPlayerInGame(authToken, gameId)) {
//                response.status(401); // Unauthorized
//                return gson.toJson(new StandardResponse(401, "Error: Unauthorized - You are not authorized to update this game"));
//            }
//
//            // Get the team color from the request
//            String teamColorParam = request.queryParams("teamColor");
//
//            // Check if the team color is invalid (e.g., not "WHITE" or "BLACK")
//            if (!isValidTeamColor(teamColorParam)) {
//                response.status(403); // Forbidden
//                return gson.toJson(new StandardResponse(403, "Error: Forbidden - Invalid team color"));
//            }
//
//            // Your implementation for updating a game
//            // Example: gameDAO.updateGame(request.body());
//            // Adapt based on how your client sends game data in the request.
//
//            // Return a success message as JSON
//            response.type("application/json");
//            JsonObject jsonResponse = new JsonObject();
//            jsonResponse.addProperty("status", "success");
//            jsonResponse.addProperty("message", "Game updated successfully");
//            return gson.toJson(jsonResponse);
//        } catch (Exception e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
//    }
//}
package handlers;

import com.google.gson.JsonObject;
import server.Server;
import server.StandardResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler extends Server implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            // Check if the request contains a valid Authorization header
            String authToken = request.headers("Authorization");
            if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                response.status(401); // Unauthorized
                return gson.toJson(new StandardResponse(401, "Error: Unauthorized"));
            }

            // Extract necessary parameters from the request
            String gameId = request.queryParams("gameId");
            String teamColorParam = request.queryParams("teamColor");

            // Call the GameService to handle the logic
            JsonObject result = gameService.join(authToken, gameId, teamColorParam);

            // Return the result as JSON
            response.type("application/json");
            return gson.toJson(result);
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }
}
