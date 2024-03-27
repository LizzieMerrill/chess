package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private int gameID;
    private ChessMove move;
    private String authToken;
    private CommandType commandType;

    public MakeMove(int gameID, ChessMove move, String authToken) {
        this.gameID = gameID;
        this.move = move;
        this.authToken = authToken;
        this.commandType = CommandType.MAKE_MOVE;
    }
}
