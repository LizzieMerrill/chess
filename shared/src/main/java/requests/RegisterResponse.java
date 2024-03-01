package requests;

public record RegisterResponse (String username, String authToken, String message) {//int statusCode)
}
