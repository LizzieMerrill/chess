package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.AuthData;

import java.util.Map;

public interface AuthDAO {
    boolean addAuthToken(AuthData authData) throws DataAccessException;
    AuthData getAuthToken(String authToken) throws DataAccessException;
    boolean removeAuthData(String authToken) throws DataAccessException;
    boolean clearAuthData() throws DataAccessException;
    boolean isValidAuthToken(String authToken) throws DataAccessException;
    Map<String, AuthData> getAuthList() throws DataAccessException;
}