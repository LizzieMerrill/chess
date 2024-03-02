package dataAccess.dao;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    public final Map<String, AuthData> authTokenMap;
    public MemoryAuthDAO() {
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
    public void removeAuthData(String authToken) {
        AuthData authData = authTokenMap.get(authToken);
        if (authData != null) {
            authTokenMap.remove(authToken);
        }
    }
    @Override
    public void clearAuthData() {
        authTokenMap.clear();
    }
    public boolean isValidAuthToken(String authToken) {
        return authTokenMap.containsKey(authToken);
    }

    @Override
    public Map<String, AuthData> getAuthList() {
        return authTokenMap;
    }
}
