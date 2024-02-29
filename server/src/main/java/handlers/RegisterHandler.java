//package handlers;
//
//import com.google.gson.Gson;
//import service.UserService;
//import dataAccess.data.UserData;
//import server.StandardResponse;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//import static jdk.internal.joptsimple.internal.Strings.isNullOrEmpty;
//
//
//public class RegisterHandler implements Route {
//    private final Gson gson;
//    private final UserService userService;
//
//    public RegisterHandler(Gson gson, UserService userService) {
//        this.gson = gson;
//        this.userService = userService;
//    }
//
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//        try {
//            UserData userData = gson.fromJson(request.body(), UserData.class);
//
//            // Check for bad request
//            if (userData.getUsername() == null || userData.getUsername().isEmpty() ||
//                    userData.getPassword() == null || userData.getPassword().isEmpty() ||
//                    userData.getEmail() == null || userData.getEmail().isEmpty()) {
//                // Handle bad request
//                response.status(400);
//                return gson.toJson(new StandardResponse(400, "Error: Bad request"));
//            }
//
//            // Check if the user already exists
//            String registrationResponse = userService.register(userData);
//
//            // Set the status code based on the response
//            response.status(getStatusCode(registrationResponse));
//
//            // Include the status code in the response body
//            return registrationResponse;
//        } catch (Exception e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
//    }
//
//
//    private int getStatusCode(String responseJson) {
//        StandardResponse standardResponse = gson.fromJson(responseJson, StandardResponse.class);
//        return standardResponse.getStatus();
//    }
//}
package handlers;

import com.google.gson.Gson;
import service.UserService;
import dataAccess.data.UserData;
import server.StandardResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import static jdk.internal.joptsimple.internal.Strings.isNullOrEmpty;

public class RegisterHandler implements Route {
    private final Gson gson;
    private final UserService userService;

    public RegisterHandler(Gson gson, UserService userService) {
        this.gson = gson;
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            UserData userData = gson.fromJson(request.body(), UserData.class);

            // Check for bad request
            if (userData.getUsername() == null || userData.getUsername().isEmpty() ||
                    userData.getPassword() == null || userData.getPassword().isEmpty() ||
                    userData.getEmail() == null || userData.getEmail().isEmpty()) {
                // Handle bad request
                response.status(400);
                return gson.toJson(new StandardResponse(400, "Error: Bad request"));
            }

            // Continue with user registration
            String registrationResponse = userService.register(userData, response);

            // Set the status code based on the response
            response.status(getStatusCode(registrationResponse));

            // Include the status code in the response body
            return registrationResponse;
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }

    private int getStatusCode(String responseJson) {
        StandardResponse standardResponse = gson.fromJson(responseJson, StandardResponse.class);
        return standardResponse.getStatus();
    }
}
