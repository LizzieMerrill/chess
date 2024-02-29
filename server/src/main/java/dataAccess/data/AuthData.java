package dataAccess.data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class AuthData {

    private String username;
    //private String password;  // Make sure to set this when adding a user
    private String authToken;
    //private Set<String> watcherTokens;

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
        //this.watcherTokens = new HashSet<>();
    }

    public AuthData(UserData userData) {
        this.authToken = UUID.randomUUID().toString();
        this.username = userData.getUsername();
        //this.watcherTokens = new HashSet<>();
    }

    // Constructors, getters, and setters

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

//    //public Object getPassword() {
//        return password;
//    }

    // New method to get the current player's authorization token
//    public String getCurrentPlayerAuthorization() {
//        return authToken;
//    }

    // New method to add a watcher token to the current player's set of watchers
//    public void addWatcherToken(String watcherToken) {
//        watcherTokens.add(watcherToken);
//    }
//
//    // Getter for the set of watcher tokens
//    public Set<String> getWatcherTokens() {
//        return watcherTokens;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return Objects.equals(username, authData.username) && Objects.equals(authToken, authData.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authToken);
    }
}
