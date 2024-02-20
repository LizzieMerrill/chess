package server;

public class StandardResponse {
    private final int status;
    private final String message;

    public StandardResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Add getters if needed
}
