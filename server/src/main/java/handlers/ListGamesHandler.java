package handlers;

import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;
import requests.ListResponse;
import server.Server;
import service.DataService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;


public class ListGamesHandler extends Server implements Route {
    UserService userService;
    DataService dataService;

    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.userService = new UserService(authDAO, userDAO);
        this.dataService = new DataService(authDAO, userDAO, gameDAO);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
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
