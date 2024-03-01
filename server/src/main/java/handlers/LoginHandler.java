//package handlers;
//
//import dataAccess.access.DataAccessException;
//import dataAccess.data.AuthData;
//import dataAccess.data.UserData;
//import server.Server;
//import server.StandardResponse;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//public class LoginHandler extends Server implements Route {
//
//
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//        try {
//            UserData userData = gson.fromJson(request.body(), UserData.class);
//
//            // Authenticate the user
//            UserData storedUserData = userDAO.getUser(userData.getUsername());
//            if (storedUserData != null && storedUserData.getPassword().equals(userData.getPassword())) {
//                AuthData existingAuthData = authDAO.getByUsername(userData.getUsername());
//
//                if (existingAuthData != null) {
//                    // Return existing token if user already has one
//                    return gson.toJson(existingAuthData);
//                } else {
//                    // Generate a new token
//                    AuthData authData = new AuthData(userData);
//                    String newAuthToken = generateUniqueAuthToken();
//                    authData.setAuthToken(newAuthToken);
//                    authDAO.addAuthToken(authData);
//                    return gson.toJson(authData);
//                }
//            } else {
//                response.status(401);
//                return gson.toJson(new StandardResponse(401, "{ \"message\": \"Error: unauthorized\" }"));
//            }
//        } catch (
//                DataAccessException e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }"));
//        }
//    }
//}
package handlers;

import com.google.gson.Gson;
import dataAccess.access.DataAccessException;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.UserDAO;
import dataAccess.data.AuthData;
import dataAccess.data.UserData;
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
//                UserData storedUserData = userService.getUserByUsername(userData.getUsername());
//            if (storedUserData != null && storedUserData.getPassword().equals(userData.getPassword())
//            && storedUserData.getUsername().equals(userData.getUsername())){
                //if (authToken == null || !userService.validate(authToken)) {


                    // Authenticate the user using the UserService
                    RegisterResponse loginResponse = userService.login(userData);

                    if(loginResponse.message().isEmpty()){
                        response.status(200);
                    }
                    else if(loginResponse.message().contains("unauthorized")){
                        response.status(401);
                    }
                    else{
                        response.status(500);
                    }


                    // Set the status code based on the response
                    //response.status(getStatusCode(loginResponse));
                    return loginResponse;
//                }
//                else{
//                    response.status(401);
//                return new Gson().toJson(new StandardResponse(401, "Error: " + e.getMessage()));
//                }
            } catch (Exception e) {
                response.status(500);
                return new Gson().toJson(new StandardResponse(500, "Error: " + e.getMessage()));
            }
        }

//        private int getStatusCode(String responseJson) {
//            StandardResponse standardResponse = gson.fromJson(responseJson, StandardResponse.class);
//            return standardResponse.getStatus();
//        }
    }