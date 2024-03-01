package dataAccess.dao;

import dataAccess.data.AuthData;
import dataAccess.data.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    //private final Map<String, AuthData> authDataMap;
    private final Map<String, AuthData> authTokenMap;


    public MemoryAuthDAO() {
        //this.authDataMap = new HashMap<>();
        this.authTokenMap = new HashMap<>();
    }

//    @Override
//    public void addAuthToken(AuthData authData) {
//        authTokenMap.put(authData.getAuthToken(), authData);
//        authDataMap.put(authData.getUsername(), authData);
//    }

    @Override
    public void addAuthToken(AuthData authData) {
        //String authToken = generateUniqueAuthToken(); // Generate a unique auth token
        //authData.setAuthToken(authToken); // Set the auth token in AuthData
        authTokenMap.put(authData.getAuthToken(), authData);
        //authDataMap.put(authData.getUsername(), authData);
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return authTokenMap.get(authToken);
    }

//    @Override
//    public String getUsernameByAuthToken(String authToken) {
//        return authTokenMap.get(authToken).getUsername();
//    }

//    @Override
//    public AuthData getByUsername(String username) {
//        // Simulate fetching AuthData by username
//        return authTokenMap.values().stream()
//                .filter(authData -> authData.getUsername().equals(username))
//                .findFirst()
//                .orElse(null);
//    }

//    @Override
//    public void addAuthData(AuthData authData) {
//        authDataMap.put(authData.getUsername(), authData);
//        addAuthToken(authData);
//    }

    @Override
    public void removeAuthData(String authToken) {
        AuthData authData = authTokenMap.get(authToken);
        if (authData != null) {
            //authDataMap.remove(authData.getUsername());
            authTokenMap.remove(authToken);
        }
    }

//    @Override
//    public boolean authenticateUser(UserData userData) {
//        AuthData authData = authTokenMap.get(userData.getUsername());
//
//        System.out.println("Received authentication request for user: " + userData.getUsername());
//        //System.out.println("Stored password: " + (authData != null ? authData.getPassword() : "null"));
//        System.out.println("Provided password: " + userData.getPassword());
//
//        // Check if the user is found and the password matches
//        if (authData != null) {
//            return true;
//        } else {
//            // Authentication failed, return false without causing a server error
//            return false;
//        }
//    }

    @Override
    public void clearAuthData() {
        //authDataMap.clear();
        authTokenMap.clear();
    }

    public boolean isValidAuthToken(String authToken) {
        return authTokenMap.containsKey(authToken);
    }
    public String generateUniqueAuthToken() {
        return UUID.randomUUID().toString();
    }

}
