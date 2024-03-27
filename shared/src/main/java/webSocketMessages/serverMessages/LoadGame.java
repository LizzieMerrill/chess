package webSocketMessages.serverMessages;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class LoadGame extends ServerMessage {
    private ChessGame game;
    public LoadGame(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    public ChessGame getGame() {
        return game;
    }
}
