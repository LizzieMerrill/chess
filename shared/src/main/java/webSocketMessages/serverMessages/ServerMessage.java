package webSocketMessages.serverMessages;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
@WebSocket
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }









    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session.getRemoteAddress());
        // Handle WebSocket connection
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        //System.out.println("WebSocket closed: " + reason);
        // Handle WebSocket close
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message from client: " + message);
        // Handle WebSocket message
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        System.err.println("WebSocket error:");
        cause.printStackTrace();
        // Handle WebSocket error
    }













    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}