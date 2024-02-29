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

import dataAccess.access.DataAccessException;
import dataAccess.data.AuthData;
import dataAccess.data.UserData;
import server.Server;
import server.StandardResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler extends Server implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            UserData userData = gson.fromJson(request.body(), UserData.class);

            // Authenticate the user using the UserService
            return userService.login(userData, response);
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new StandardResponse(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }"));
        }
    }
}
