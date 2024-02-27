package dataAccess.dao;

import dataAccess.data.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authDataMap;

    public MemoryAuthDAO() {
        this.authDataMap = new HashMap<>();
    }

    @Override
    public AuthData getByAuthToken(String authToken) {
        // Placeholder implementation, replace with actual logic
        return authDataMap.get(authToken);
    }

    @Override
    public AuthData getByUsername(String username) {
        // Placeholder implementation, replace with actual logic
        return authDataMap.values().stream()
                .filter(authData -> authData.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addAuthData(AuthData authData) {
        // Placeholder implementation, replace with actual logic
        authDataMap.put(authData.getAuthToken(), authData);
    }

    @Override
    public void removeAuthData(String authToken) {
        // Placeholder implementation, replace with actual logic
        authDataMap.remove(authToken);
    }
    @Override
    public void addAuthToken(AuthData authData) {
        authDataMap.put(authData.getAuthToken(), authData);
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return authDataMap.get(authToken);
    }


}
