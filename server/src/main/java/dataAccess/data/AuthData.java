package dataAccess.data;

import java.util.Objects;
import java.util.UUID;

public class AuthData {

    private String username;
    private String password;  // Make sure to set this when adding a user
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

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public Object getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return Objects.equals(username, authData.username) && Objects.equals(password, authData.password) && Objects.equals(authToken, authData.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, authToken);
    }
}
