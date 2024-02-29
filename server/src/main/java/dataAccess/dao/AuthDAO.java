package dataAccess.dao;

import dataAccess.data.AuthData;
import dataAccess.data.UserData;

public interface AuthDAO {
    void addAuthToken(AuthData authData);
    AuthData getAuthToken(String authToken);
    AuthData getByAuthToken(String authToken);
    AuthData getByUsername(String username);
    //void addAuthData(AuthData authData);
    void removeAuthData(String authToken);
    //boolean authenticateUser(String username, String password);
    boolean authenticateUser(UserData userData);
    void clearAuthData();

    boolean isValidAuthToken(String authToken);
}