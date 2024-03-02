package model;

import java.util.Objects;
import java.util.UUID;

public class AuthData {

    private String username;
    private String authToken;

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public AuthData(UserData userData) {
        this.authToken = UUID.randomUUID().toString();
        this.username = userData.getUsername();
    }

    // Constructors, getters, and setters

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

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
