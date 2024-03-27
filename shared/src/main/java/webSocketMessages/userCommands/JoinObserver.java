package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {
    private int gameID;
    private String authToken;
    private CommandType commandType;

    public JoinObserver(int gameID, String authToken) {
        this.gameID = gameID;
        this.authToken = authToken;
        this.commandType = CommandType.JOIN_OBSERVER;
    }
}
