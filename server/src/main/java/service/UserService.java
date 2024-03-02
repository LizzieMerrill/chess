package service;

import dataAccess.dao.AuthDAO;
import dataAccess.dao.SQLUserDAO;
import dataAccess.dao.UserDAO;
import model.AuthData;
import model.UserData;
import requests.ErrorObject;
import requests.RegisterResponse;

public class UserService {

    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }


    public RegisterResponse login(UserData user) {
        try {
            UserData storedUserData = userDAO.getUser(user.getUsername());

            if (storedUserData != null && storedUserData.getPassword().equals(user.getPassword())) {
                AuthData authData = new AuthData(storedUserData);
                authDAO.addAuthToken(authData);
                return new RegisterResponse(user.getUsername(), authData.getAuthToken(), null);//return authData;//return gson.toJson(new StandardResponse(200, storedUserData));
            } else {
                return new RegisterResponse(null, null, "Error: unauthorized");//return gson.toJson(new StandardResponse(401, "Error: unauthorized"));//(null, null, message unauthorize, 401)
            }
        } catch (Exception e) {
            return new RegisterResponse(null, null, "Error: " + e.getMessage());
        }
    }

    public RegisterResponse register(UserData user) {
        try {
            UserData existingUser = userDAO.getUser(user.getUsername());//checks if user was already in database

            if (existingUser != null) {
                return new RegisterResponse(null, null, "Error: already taken");
            }
            else if (user.getUsername() == null || user.getUsername().isEmpty() ||
                    user.getPassword() == null || user.getPassword().isEmpty() ||
                    user.getEmail() == null || user.getEmail().isEmpty()){
                return new RegisterResponse(null, null, "Error: bad request");
            }
            else{
                AuthData authData = new AuthData(user);
                //String authDataJson = gson.toJson(authData);
                userDAO.addUser(user);
                authDAO.addAuthToken(authData);
                return new RegisterResponse(user.getUsername(), authData.getAuthToken(), null);
            }
        } catch (Exception e) {
            return new RegisterResponse(null, null, "Error: " + e.getMessage());
        }
    }



    public ErrorObject logout(String authToken) {
        try {
            if (authDAO.isValidAuthToken(authToken)) {
                authDAO.removeAuthData(authToken);
                return new ErrorObject(null);
            } else {
                return new ErrorObject("Error: unauthorized");
            }
        } catch (Exception e) {
            // Handle exceptions
            return new ErrorObject( "Error: " + e.getMessage());
        }
    }
}
