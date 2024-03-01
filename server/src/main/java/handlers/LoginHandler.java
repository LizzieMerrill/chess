package handlers;

import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.UserDAO;
import model.UserData;
import requests.RegisterResponse;
import server.Server;
import server.StandardResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler extends Server implements Route {


    private final UserService userService;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {

        this.userService = new UserService(authDAO, userDAO);
    }
        @Override
        public Object handle(Request request, Response response) throws Exception {
            try {
                UserData userData = new Gson().fromJson(request.body(), UserData.class);
                    RegisterResponse loginResponse = userService.login(userData);

                    if(loginResponse.message() == null){
                        response.status(200);
                    }
                    else if(loginResponse.message().contains("unauthorized")){
                        response.status(401);
                    }
                    else{
                        response.status(500);
                    }

                    return new Gson().toJson(loginResponse);

            } catch (Exception e) {
                response.status(500);
                return new Gson().toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
    }
}