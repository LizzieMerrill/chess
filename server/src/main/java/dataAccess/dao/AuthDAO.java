package dataAccess.dao;

import model.AuthData;

import java.util.Map;

public interface AuthDAO {
    void addAuthToken(AuthData authData);
    AuthData getAuthToken(String authToken);
    void removeAuthData(String authToken);
    void clearAuthData();
    boolean isValidAuthToken(String authToken);
    Map<String, AuthData> getAuthList();
}