package webSocketMessages.userCommands;

//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.*;

//import org.eclipse.jetty.websocket.api.annotations.*;

//import javax.websocket.Session;
import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }
    public UserGameCommand(String authToken, CommandType commandType) {
        this.authToken = authToken;
        this.commandType = commandType;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        REDRAW_CHESS_BOARD,
        HIGHLIGHT_LEGAL_MOVES
    }

    protected CommandType commandType;

    final String authToken;
//    protected int gameID;
//    protected ChessGame.TeamColor playerColor;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
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