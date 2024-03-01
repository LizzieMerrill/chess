package handlers;

import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.UserDAO;
import requests.RegisterResponse;
import service.UserService;
import model.UserData;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    private final Gson gson;
    private final UserService userService;

    public RegisterHandler(Gson gson, UserDAO userDAO, AuthDAO authDAO) {
        this.gson = gson;
        this.userService = new UserService(authDAO, userDAO);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
            UserData userData = gson.fromJson(request.body(), UserData.class);
            RegisterResponse registerResponse = userService.register(userData);
            if (registerResponse.message() == null){
                response.status(200);
            }
            else if (registerResponse.message().contains("Error: bad request")) {
                response.status(400);
            }
            else if (registerResponse.message().contains("Error: already taken")){
                response.status(403);
            }
            else{
                response.status(500);
            }

            return new Gson().toJson(registerResponse);

    }
}
