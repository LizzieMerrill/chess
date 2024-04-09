package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private int gameID;
    private ChessGame.TeamColor playerColor;
    //private CommandType commandType;

    public JoinPlayer(int gameID, ChessGame.TeamColor playerColor, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }
    public int getGameID() {
        return gameID;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
