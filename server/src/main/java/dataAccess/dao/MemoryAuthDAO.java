package dataAccess.dao;

import dataAccess.data.AuthData;
import dataAccess.data.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private final Map<String, AuthData> authDataMap;
    private final Map<String, AuthData> authTokenMap;

    public MemoryAuthDAO() {
        this.authDataMap = new HashMap<>();
        this.authTokenMap = new HashMap<>();
    }

    @Override
    public void addAuthToken(AuthData authData) {
        authTokenMap.put(authData.getAuthToken(), authData);
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return authTokenMap.get(authToken);
    }

    @Override
    public AuthData getByAuthToken(String authToken) {
        return authTokenMap.get(authToken);
    }

    @Override
    public AuthData getByUsername(String username) {
        // Simulate fetching AuthData by username
        return authDataMap.values().stream()
                .filter(authData -> authData.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addAuthData(AuthData authData) {
        authDataMap.put(authData.getUsername(), authData);
    }

    @Override
    public void removeAuthData(String authToken) {
        AuthData authData = authTokenMap.get(authToken);
        if (authData != null) {
            authDataMap.remove(authData.getUsername());
            authTokenMap.remove(authToken);
        }
    }

//    @Override
//    public boolean authenticateUser(String username, String password) {
//        // Simulate user authentication in memory
//        AuthData authData = getByUsername(username);
//        return authData != null && authData.getPassword().equals(password);
//    }

    @Override
    public boolean authenticateUser(UserData userData) {
        AuthData authData = authDataMap.get(userData.getUsername());
        return authData != null && authData.getPassword().equals(userData.getPassword());
    }
}
