package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    private String message;
    private ServerMessageType serverMessageType;

    public Notification(String message) {
        this.message = message;
        this.serverMessageType = ServerMessageType.NOTIFICATION;
    }
}
