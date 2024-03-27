package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    private int gameID;
    private String authToken;
    private CommandType commandType;

    public Leave(int gameID, String authToken) {
        this.gameID = gameID;
        this.authToken = authToken;
        this.commandType = CommandType.LEAVE;
    }
}
