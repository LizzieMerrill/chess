package dataAccess.data;

public class AuthData {
    private String authToken;
    private String username;
    private String password;  // Added password field

    // Constructors
    public AuthData() {
        // Default constructor
        authToken = null;
        username = null;
        password = null;  // Initialize password
    }
    public AuthData(String authToken, String username){
        this.authToken = authToken;
        this.username = username;
    }

    public AuthData(String authToken, String username, String password) {
        this.authToken = authToken;
        this.username = username;
        this.password = password;  // Set password
    }

    // Getters
    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
