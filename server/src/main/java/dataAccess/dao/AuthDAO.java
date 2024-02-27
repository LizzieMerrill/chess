package dataAccess.dao;

import dataAccess.data.AuthData;

public interface AuthDAO {
    void addAuthToken(AuthData authData);
    AuthData getAuthToken(String authToken);
    AuthData getByAuthToken(String authToken);
    AuthData getByUsername(String username);
    void addAuthData(AuthData authData);
    void removeAuthData(String authToken);
}