package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private int gameID;
    private ChessGame.TeamColor playerColor;
    //private CommandType commandType;

    public JoinPlayer(String authToken, CommandType commandType, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken, commandType, gameID, playerColor);
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
