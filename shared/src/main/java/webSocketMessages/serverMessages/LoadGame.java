package webSocketMessages.serverMessages;

import chess.ChessGame;
import dataAccess.access.DataAccessException;
import dataAccess.dao.GameDAO;
import dataAccess.dao.SQLGameDAO;
import model.GameData;
import webSocketMessages.userCommands.UserGameCommand;

public class LoadGame extends ServerMessage {
    private int game;
    GameDAO gameDAO = new SQLGameDAO();
    public LoadGame(int game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;//gameId
    }
    public LoadGame(String errorMessage){
        super(ServerMessageType.LOAD_GAME);
        Error error = new Error(errorMessage);
    }
    public GameData getGame() throws DataAccessException {
        return gameDAO.getGame(game);
    }
}
