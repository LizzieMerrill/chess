package webSocketMessages.userCommands;


import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
@WebSocket
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    final String authToken;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
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
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}