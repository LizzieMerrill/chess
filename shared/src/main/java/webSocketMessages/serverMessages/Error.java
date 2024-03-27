package webSocketMessages.serverMessages;

public class Error extends ServerMessage {
    private String errorMessage;
    private ServerMessageType serverMessageType;

    public Error(String errorMessage) {
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }
}
