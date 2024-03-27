package webSocketMessages.serverMessages;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class LoadGame extends ServerMessage {
    private ChessGame game;
    private ServerMessageType serverMessageType;

    public LoadGame(ChessGame game) {
        this.game = game;
        this.serverMessageType = ServerMessageType.LOAD_GAME;
    }
}
