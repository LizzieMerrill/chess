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

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;
import dataAccess.data.GameData;
import dataAccess.data.UserData;
import requests.JoinObject;
import requests.JoinResponse;
import server.Server;
import server.StandardResponse;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler extends Server implements Route {
    UserService userService;
    GameService gameService;
    private final Gson gson = new Gson();

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
//        this.authDAO = authDAO;
//        this.userDAO = userDAO;
//        this.gameDAO = gameDAO;
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(authDAO, userDAO, gameDAO);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //try {
            JoinObject joinObject = gson.fromJson(request.body(), JoinObject.class);
            int gameId = joinObject.gameID();
            ChessGame.TeamColor teamColorParam = joinObject.playerColor();

            // Check if the request contains a valid Authorization header
            String authToken = request.headers("Authorization");
            JoinResponse result = gameService.join(authToken, gameId, teamColorParam);

            if(result.message().isEmpty()){
                response.status(200);
            }
            else if (result.message().contains("Error: bad request")) {
                response.status(400); //bad request
            }
            else if (result.message().contains("Error: unauthorized")) {
                response.status(401); // Unauthorized
            }
            else if (result.message().contains("Error: already taken")) {
                response.status(403); // Unauthorized
            }
            else{
                response.status(500);
            }
            //else if else if bla bla
            return new Gson().toJson(result);


            // Return the result as JSON
            //response.status(200);
//            response.type("application/json");
//            return new Gson().toJson(result);
//        } catch (Exception e) {
//
//            return new Gson().toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
    }
}
