package dataAccess.data;

public class AuthData {
    private String authToken;
    private String username;

    // Constructors
    public AuthData() {
        // Default constructor
        authToken = null;
        username = null;
    }

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    // Getters
    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    // Setters
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
