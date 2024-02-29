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

import server.Server;
import server.StandardResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler extends Server implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String authToken = request.headers("Authorization");
            if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                response.status(401);
                return gson.toJson(new StandardResponse(401, "Error: unauthorized"));
            }

            // Logout the user using the UserService
            return userService.logout(authToken);
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }
}
