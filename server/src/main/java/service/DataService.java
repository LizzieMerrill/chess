//package service;
//
//import com.google.gson.JsonArray;
//
//public class DataService {
//
//    // Logic and DB stuff
//
//    public void clear() {
//        // Call DAO clear method
//        gameDAO.clearChessData();
//    }
//
//    public JsonArray listGames() {
//        // Call DAO method to get the list of games
//        return gameDAO.getAllGames();
//    }
//}
package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.MemoryGameDAO;
import dataAccess.dao.UserDAO;
import dataAccess.data.GameData;
import requests.ErrorObject;
import server.StandardResponse;
import spark.Response;

import java.util.Collection;
import java.util.List;

public class DataService {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    public DataService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }
    private final Gson gson = new Gson();
    //private final GameDAO gameDAO = new MemoryGameDAO();  // You can adjust this based on your actual implementation

    public ErrorObject clear() {
        JsonObject response = new JsonObject();
        try {
            // Your logic here
            gameDAO.clearChessData();
            userDAO.clearUserData();
            authDAO.clearAuthData();
            // Additional logic if needed
            response.addProperty("status", "success");
            response.addProperty("message", "Chess data cleared successfully");
        } catch (Exception e) {
            // Handle exceptions
            response.addProperty("status", "error");
            response.addProperty("message", "Error: " + e.getMessage());
        }
        return response;
    }

    public Object listGames(String authToken, Response response) {
//        if(authDAO.isValidAuthToken(authToken)){
//
//        }
        Collection<GameData> gamesList = null;
            try {
                if(authDAO.isValidAuthToken(authToken)) {
                    gamesList = gameDAO.getAllGames();
                    //return null;//TODO
                    //make copy of gamelist
                    //200 success
                    response.status(200);
//                    for(GameData game : gamesList){
//
//                    }
                    return gamesList;
                }
                else{
                    response.status(401);
                    return gson.toJson(new StandardResponse(401, "Error: unauthorized"));
                }
//
//                    // Check if there are no games
//                    if (gamesList == null || gamesList.size() == 0) {
//                        JsonObject noGamesObject = new JsonObject();
//                        noGamesObject.addProperty("noGames", true);
//                        gamesList.add(noGamesObject);
//                    } else {
//                        // Format the response as required
//                        JsonArray formattedGamesList = new JsonArray();
//                        for (JsonElement gameElement : gamesList) {
//                            JsonObject gameObject = gameElement.getAsJsonObject();
//                            JsonObject formattedGame = new JsonObject();
//                            formattedGame.addProperty("gameID", gameObject.get("gameID").getAsInt());
//                            formattedGame.addProperty("whiteUsername", gameObject.has("whiteUsername") ? gameObject.get("whiteUsername").getAsString() : null);
//                            formattedGame.addProperty("blackUsername", gameObject.has("blackUsername") ? gameObject.get("blackUsername").getAsString() : null);
//                            formattedGame.addProperty("gameName", gameObject.has("gameName") ? gameObject.get("gameName").getAsString() : "");
//                            formattedGamesList.add(formattedGame);
//                        }
//                        return formattedGamesList;
//                    }
//                }
//                else{
//                    response.status(401);
//                }
                // Additional logic if needed
            } catch (Exception e) {
                // Handle exceptions
//                GameData errorObject = new JsonObject();
//                errorObject.addProperty("error", "Error: " + e.getMessage());
//                gamesList.add(errorObject);
                response.status(500);
                return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
    }

    public boolean validate(String authToken){
        return authDAO.isValidAuthToken(authToken);
    }

}
