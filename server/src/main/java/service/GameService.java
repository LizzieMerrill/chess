package service;

import com.google.gson.JsonObject;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;

public class GameService {

    private GameDAO gameDAO;

//   // public GameService(GameDAO gameDAO) {
//        this.gameDAO = gameDAO;
//    }
    private AuthDAO authDAO;
    private UserDAO userDAO;


// //   public GameService(AuthDAO authDAO) {
//        this.authDAO = authDAO;
//    }

    public GameService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public JsonObject join(String authToken, String gameId, String teamColor) {
        try {
            // Check if the request contains a valid Authorization header
            if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                return createErrorResponse(401, "Error: Unauthorized");
            }

            // Check if the game ID in the request is valid (e.g., not null or empty)
            if (isNullOrEmpty(gameId)) {
                return createErrorResponse(400, "Error: Bad Request - Invalid Game ID");
            }

            // Check if the user is authorized to update the game
            if (!gameDAO.isPlayerInGame(authToken, gameId)) {
                return createErrorResponse(401, "Error: Unauthorized - You are not authorized to update this game");
            }

            // Check if the team color is invalid (e.g., not "WHITE" or "BLACK")
            if (!isValidTeamColor(teamColor)) {
                return createErrorResponse(403, "Error: Forbidden - Invalid team color");
            }

            // Your implementation for updating a game
            // Example: gameDAO.updateGame(request.body());
            // Adapt based on how your client sends game data in the request.

            // Check if there are one or more spectators
            int spectatorCount = gameDAO.getSpectatorCount(gameId);

            // Check if people are joining an already created game
            boolean isGameCreated = gameDAO.isGameCreated(gameId);

            // Your additional logic here...

            // Return a success message as JSON
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Game updated successfully");
            jsonResponse.addProperty("spectatorCount", spectatorCount);
            jsonResponse.addProperty("isGameCreated", isGameCreated);
            return jsonResponse;

        } catch (Exception e) {
            return createErrorResponse(500, "Error: " + e.getMessage());
        }
    }

    public JsonObject create(String authToken, String gameData) {
        try {
            // Check if the request contains a valid Authorization header
            if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                return createErrorResponse(401, "Error: Unauthorized");
            }

            // Your implementation for creating a game
            // Example: gameDAO.createGame(authToken, gameData);
            // Adapt based on how your client sends create game data.

            // Return a success message as JSON with the gameID
            int gameID = 1234; // Replace with the actual game ID generated
            JsonObject successResponse = new JsonObject();
            successResponse.addProperty("gameID", gameID);
            return createSuccessResponse(successResponse);
        } catch (Exception e) {
            return createErrorResponse(500, "Error: " + e.getMessage());
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidTeamColor(String teamColor) {
        return teamColor == null || ("WHITE".equals(teamColor) || "BLACK".equals(teamColor));
    }

    private JsonObject createSuccessResponse(String message) {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("message", message);
        return jsonResponse;
    }

    private JsonObject createSuccessResponse(JsonObject data) {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "success");
        jsonResponse.add("data", data);
        return jsonResponse;
    }

    private JsonObject createErrorResponse(int status, String message) {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "error");
        jsonResponse.addProperty("message", message);
        return jsonResponse;
    }
    public boolean validate(String authToken){
        return authDAO.isValidAuthToken(authToken);
    }
}
