package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;
import dataAccess.data.GameData;
import requests.ListResponse;
import server.Server;
import server.StandardResponse;
import service.DataService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ListGamesHandler extends Server implements Route {
    //    AuthDAO authDAO;
//    UserDAO userDAO;
//    GameDAO gameDAO;
    UserService userService;
    DataService dataService;
    private final Gson gson = new Gson();

    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
//        this.authDAO = authDAO;
//        this.userDAO = userDAO;
//        this.gameDAO = gameDAO;
        this.userService = new UserService(authDAO, userDAO);
        this.dataService = new DataService(authDAO, userDAO, gameDAO);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //Collection<GameData> gamesList = dataService.listGames(request.headers("Authorization")).games();
        ListResponse listResponse = dataService.listGames(request.headers("Authorization"));
        if (listResponse.message() == null) {
            response.status(200);
        } else if (listResponse.message().contains("Error: unauthorized")) {
            response.status(401);
        } else {
            response.status(500);
        }
        return new Gson().toJson(listResponse);
    }
}
//            // Check if there are no games
//            if (gamesList == null || gamesList.isEmpty()) {
//                response.type("application/json");
//                response.status(200);
//                return "{ \"noGames\": true }";
//            } else{
//
//
//                // Check if there are multiple games
//                response.type("application/json");
//
//                // Convert the JsonArray to a list and sort it based on gameID
//                //List<JsonObject> sortedGamesList = gson.fromJson(gamesList, new TypeToken<List<JsonObject>>() {}.getType());
//
//                if (gamesList.size() >= 1) {
//                    // If there are multiple games, sort and return the list
//                    //gamesList.sort(Comparator.comparingInt(o -> o.get("gameID").getAsInt()));
//                    response.status(200);
//                    return gson.toJson(gamesList);
//
//                } else {
//                    // If there are no games
//                    response.status(200);
//                    return "{ \"noGames\": true }";
//                }
//            }
////            else if (authDAO.isValidAuthToken(request.headers("Authorization")) == false){
////                response.status(401);
////                return gson.toJson(new StandardResponse(401, "Error: unauthorized"));
////            }
//
//            //response.status(200);
//        } catch (Exception e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
//        response.status(200);
//        return gson.toJson(gamesList);


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        ListGamesHandler that = (ListGamesHandler) o;
//        return Objects.equals(userService, that.userService) && Objects.equals(dataService, that.dataService) && Objects.equals(gson, that.gson);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), userService, dataService, gson);
//    }








//
//package handlers;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//import dataAccess.dao.AuthDAO;
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
//    AuthDAO authDAO;
//
//    public ListGamesHandler(AuthDAO authDAO) {
//        this.authDAO = authDAO;
//    }
//
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//        try {
//            // Call the service method to get the list of games
//            JsonArray gamesList = dataService.listGames(request.headers("Authorization"), response);
//
//            // Check if there are no games
//            if (gamesList == null || gamesList.isEmpty()) {
//                response.status(200);
//                response.type("application/json");
//                return gson.toJson(new StandardResponse(200, "{ \"noGames\": true }"));
//            } else if (authDAO.isValidAuthToken(request.headers("Authorization"))) {
//                // Check if there are multiple games
//                response.type("application/json");
//
//                // Convert the JsonArray to a list and sort it based on gameID
//                List<JsonObject> sortedGamesList = gson.fromJson(gamesList, new TypeToken<List<JsonObject>>() {}.getType());
//
//                if (sortedGamesList != null && sortedGamesList.size() > 1) {
//                    // If there are multiple games, sort and return the list
//                    sortedGamesList.sort(Comparator.comparingInt(o -> o.get("gameID").getAsInt()));
//                    response.status(200);
//                    return gson.toJson(sortedGamesList);
//                } else if (sortedGamesList != null && sortedGamesList.size() == 1) {
//                    // If there is exactly one game, return it without sorting
//                    response.status(200);
//                    return gson.toJson(sortedGamesList.get(0));
//                } else {
//                    // If there are no games
//                    response.status(200);
//                    return "{ \"noGames\": true }";
//                }
//            } else {
//                // If the authorization token is not valid
//                response.status(401);
//                return gson.toJson(new StandardResponse(401, "Error: unauthorized"));
//            }
//        } catch (Exception e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
//    }
//}
