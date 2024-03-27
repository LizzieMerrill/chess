package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {
    private int gameID;
    //private CommandType commandType;

    public JoinObserver(int gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }
    public int getGameID() {
        return gameID;
    }
}
