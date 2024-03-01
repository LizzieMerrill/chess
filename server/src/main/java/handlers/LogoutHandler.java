package handlers;

import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import requests.ErrorObject;
import server.Server;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;

public class LogoutHandler extends Server implements Route {

    private final UserService userService;

    public LogoutHandler(AuthDAO authDAO) {
        this.userService = new UserService(authDAO, null);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
            String authToken = request.headers("Authorization");
            ErrorObject returnValue = userService.logout(authToken);

            if (returnValue.message() == null) {
                response.status(200);
            }
            else if (returnValue.message().contains("Error: unauthorized")){
                response.status(401);
            }
            else{
                response.status(500);
            }
        return new Gson().toJson(returnValue);
    }
}
