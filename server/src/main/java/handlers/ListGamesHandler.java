//package handlers;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//import server.Server;
//import server.StandardResponse;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//import java.util.Comparator;
//import java.util.List;
//
//public class ListGamesHandler extends Server implements Route {
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//        try {
//            // Your implementation for handling the GET request for /game
//
//            // Get the list of games
//            JsonArray gamesList = gameDAO.getAllGames();
//
//            // Check if there are no games
//            if (gamesList == null || gamesList.isEmpty()) {
//                response.type("application/json");
//                return "{ \"noGames\": true }";
//            }
//
//            // Check if there are multiple games
//            response.type("application/json");
//
//            // Convert the JsonArray to a list and sort it based on gameID
//            List<JsonObject> sortedGamesList = gson.fromJson(gamesList, new TypeToken<List<JsonObject>>() {}.getType());
//            sortedGamesList.sort(Comparator.comparingInt(o -> o.get("gameID").getAsInt()));
//
//            return gson.toJson(new StandardResponse(200, sortedGamesList.toString()));
//        } catch (Exception e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
//    }
//}
package handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import server.Server;
import server.StandardResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Comparator;
import java.util.List;

public class ListGamesHandler extends Server implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            // Call the service method to get the list of games
            JsonArray gamesList = dataService.listGames();

            // Check if there are no games
            if (gamesList == null || gamesList.isEmpty()) {
                response.type("application/json");
                return "{ \"noGames\": true }";
            }

            // Check if there are multiple games
            response.type("application/json");

            // Convert the JsonArray to a list and sort it based on gameID
            List<JsonObject> sortedGamesList = gson.fromJson(gamesList, new TypeToken<List<JsonObject>>() {}.getType());
            sortedGamesList.sort(Comparator.comparingInt(o -> o.get("gameID").getAsInt()));

            return gson.toJson(new StandardResponse(200, sortedGamesList.toString()));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }
}
