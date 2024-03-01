//package handlers;
//
//import dataAccess.access.DataAccessException;
//import server.Server;
//import server.StandardResponse;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//public class CreateGameHandler extends Server implements Route {
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
//            // Your implementation for creating a game
//            // Example: gameDAO.createGame(request.body());
//            // Adapt based on how your client sends game data in the request.
//
//            // For simplicity, let's assume createGame returns the created game ID
//            int gameId = gameDAO.createGame(request.body());
//
//            return gson.toJson(new StandardResponse(200, "Game created successfully. Game ID: " + gameId));
//
//
//        } catch (DataAccessException e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        } catch (Exception e) {
//            response.status(403); // Forbidden
//            return gson.toJson(new StandardResponse(403, "Error: Invalid team color"));
//        }
//    }
//}
package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;
import requests.CreateResponse;
import server.Server;
import server.StandardResponse;
import service.DataService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler extends Server implements Route {

    UserService userService;
    GameService gameService;

    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
//        this.authDAO = authDAO;
//        this.userDAO = userDAO;
//        this.gameDAO = gameDAO;
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(authDAO, userDAO, gameDAO);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
            // Check if the request contains a valid Authorization header
            String authToken = request.headers("Authorization");
            CreateResponse result = gameService.create(authToken, request.body());//authtoken and game name input
            if(result.message() == null){
                response.status(200);
            }
            else if (result.message().contains("Error: unauthorized")) {
                response.status(401); // Unauthorized
            }
            else if (result.message().contains("Error: bad request")){
                response.status(403);
            }
            else{
                response.status(500);
            }

            return new Gson().toJson(result);
    }
}
