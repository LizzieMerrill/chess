package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private int gameID;
    private String authToken;
    private CommandType commandType;

    public ResignCommand(int gameID, String authToken) {
        this.gameID = gameID;
        this.authToken = authToken;
        this.commandType = CommandType.RESIGN;
    }
}
