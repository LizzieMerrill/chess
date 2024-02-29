//package handlers;
//
//import server.Server;
//import server.StandardResponse;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//public class LogoutHandler extends Server implements Route {
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//        try {
//            String authToken = request.headers("Authorization");
//            if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
//                response.status(401);
//                return gson.toJson(new StandardResponse(401, "Error: unauthorized"));
//            }
//
//
//            authDAO.removeAuthData(authToken);
//            return gson.toJson(new StandardResponse(200, "Logout successful"));
//        } catch (Exception e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
//    }
//}
package handlers;

import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.UserDAO;
import server.Server;
import server.StandardResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler extends Server implements Route {

    private final UserService userService;

    public LogoutHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userService = new UserService(authDAO, userDAO);
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String authToken = request.headers("Authorization");
            if(!(authToken == null || !userService.validate(authToken))){
                response.status(200);
                // Logout the user using the UserService
                return userService.logout(authToken);
            }
            else if (authToken == null || !userService.validate(authToken)) {
                response.status(401);
                return new Gson().toJson(new StandardResponse(401, "Error: unauthorized"));
            }
            else{
                // Logout the user using the UserService
                return userService.logout(authToken);
            }
        } catch (Exception e) {
            response.status(500);
            return new Gson().toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }
}
