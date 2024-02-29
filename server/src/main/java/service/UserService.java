//package service;
//
//import com.google.gson.Gson;
//import dataAccess.access.DataAccessException;
//import dataAccess.data.AuthData;
//import dataAccess.data.UserData;
//import server.StandardResponse;
//
//public class UserService {
//
//    private final Gson gson;
//
//    public UserService(Gson gson) {
//        this.gson = gson;
//    }
//    public Object register(UserData user) {
//        try {
//            // Your logic here for registering a user
//            // ...
//
//            AuthData authData = new AuthData(user);
//            // Additional logic if needed
//
//            //return authData;
//            String authDataJson = gson.toJson(authData);
//            return gson.toJson(new StandardResponse(200, authDataJson));
//        } catch (Exception e) {
//            // Handle exceptions
//            return new StandardResponse(500, "Error: " + e.getMessage());
//        }
//    }
//
//    public Object login(UserData user) {
////        try {
////            // Your logic here for logging in a user
////            // ...
////
////            AuthData authData = new AuthData(user);
////            // Additional logic if needed
////
////            String authDataJson = gson.toJson(authData);
////            return gson.toJson(new StandardResponse(200, authDataJson));
////        } catch (Exception e) {
////            // Handle exceptions
////            return new StandardResponse(500, "Error: " + e.getMessage());
////        }
//        try {
//            // Your logic here for logging in a user
//            UserData storedUserData = // Retrieve user data from the database based on the provided credentials
//
//            if (storedUserData != null) {
//                // Login successful, return user information
//                AuthData authData = new AuthData(storedUserData);
//                String authDataJson = gson.toJson(authData);
//                return gson.toJson(new StandardResponse(200, authDataJson));
//            } else {
//                // Login failed, return an appropriate response
//                return new StandardResponse(401, "Error: Invalid credentials");
//            }
//        } catch (Exception e) {
//            // Handle exceptions
//            return new StandardResponse(500, "Error: " + e.getMessage());
//        }
//    }
//
//    public Object logout(String authToken) {
//        try {
//            // Your logic here for logging out a user
//            // ...
//
//            // Additional logic if needed
//
//            return new StandardResponse(200, "Logout successful");
//        } catch (Exception e) {
//            // Handle exceptions
//            return new StandardResponse(500, "Error: " + e.getMessage());
//        }
//    }
//}
package service;

import com.google.gson.Gson;
import dataAccess.dao.UserDAO;
import dataAccess.data.AuthData;
import dataAccess.data.UserData;
import server.StandardResponse;
import spark.Response;

public class UserService {

    private final Gson gson;
    private final UserDAO userDAO; // Inject the UserDAO dependency

    public UserService(Gson gson, UserDAO userDAO) {
        this.gson = gson;
        this.userDAO = userDAO;
    }

    public Object login(UserData user, Response response) {
        try {
            // Your logic here for logging in a user
            UserData storedUserData = userDAO.getUser(user.getUsername());

            if (storedUserData != null && storedUserData.getPassword().equals(user.getPassword())) {
                // Login successful, return user information
                AuthData authData = new AuthData(storedUserData);
                String authDataJson = gson.toJson(authData);
                response.status(200);
                return authDataJson; // Return only the JSON response, not wrapped in StandardResponse
            } else {
                // Login failed, return an appropriate response
                response.status(401);
                return gson.toJson(new StandardResponse(401, "Error: Invalid credentials"));
            }
        } catch (Exception e) {
            // Handle exceptions
            response.status(500);
            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }



    public Object logout(String authToken) {
        try {
            // Your logic here for logging out a user
            // ...

            // Additional logic if needed

            return new StandardResponse(200, "Logout successful");
        } catch (Exception e) {
            // Handle exceptions
            return new StandardResponse(500, "Error: " + e.getMessage());
        }
    }
    public String register(UserData user, Response response) {
        try {
            // Your logic here for registering a user
            UserData existingUser = userDAO.getUser(user.getUsername());

            // Check if the user already exists
            if (existingUser != null) {
                response.status(403);
                return gson.toJson(new StandardResponse(403, "Error: User already exists"));
            }

            AuthData authData = new AuthData(user);
            // Additional logic if needed

            String authDataJson = gson.toJson(authData);
            return gson.toJson(new StandardResponse(200, authDataJson));
        } catch (Exception e) {
            // Handle exceptions
            response.status(500);
            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }




}
