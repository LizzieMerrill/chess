package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private int gameID;
    private ChessGame.TeamColor playerColor;
    private String authToken;
    private CommandType commandType;

    public JoinPlayer(int gameID, ChessGame.TeamColor playerColor, String authToken) {
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.authToken = authToken;
        this.commandType = CommandType.JOIN_PLAYER;
    }
}
