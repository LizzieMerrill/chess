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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataAccess.dao.GameDAO;
import dataAccess.dao.MemoryGameDAO;

public class DataService {

    private final GameDAO gameDAO = new MemoryGameDAO();  // You can adjust this based on your actual implementation

    public JsonObject clear() {
        JsonObject response = new JsonObject();
        try {
            // Your logic here
            gameDAO.clearChessData();
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

    public JsonArray listGames() {
        JsonArray gamesList = new JsonArray();
        try {
            // Your logic here
            gamesList = gameDAO.getAllGames();
            // Additional logic if needed
        } catch (Exception e) {
            // Handle exceptions
            JsonObject errorObject = new JsonObject();
            errorObject.addProperty("error", "Error: " + e.getMessage());
            gamesList.add(errorObject);
        }
        return gamesList;
    }
}
