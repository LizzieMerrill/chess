package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.AuthData;

import java.util.Map;

public interface AuthDAO {
    void addAuthToken(AuthData authData) throws DataAccessException;
    AuthData getAuthToken(String authToken) throws DataAccessException;
    void removeAuthData(String authToken) throws DataAccessException;
    void clearAuthData() throws DataAccessException;
    boolean isValidAuthToken(String authToken) throws DataAccessException;
    Map<String, AuthData> getAuthList() throws DataAccessException;
}