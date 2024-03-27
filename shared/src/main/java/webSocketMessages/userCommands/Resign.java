package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private int gameID;
    //private CommandType commandType;

    public Resign(int gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.RESIGN;
    }
    public int getGameID() {
        return gameID;
    }
}
