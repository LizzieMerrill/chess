package handlers;

import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;
import requests.CreateResponse;
import server.Server;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler extends Server implements Route {

    UserService userService;
    GameService gameService;

    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
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
