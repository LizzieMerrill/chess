package dataAccess.dao;

import chess.ChessGame;
import dataAccess.access.DataAccessException;
import model.GameData;
import java.util.Collection;
import java.util.Map;


public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    void clearChessData() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    void updateGame(GameData gameData, String username) throws DataAccessException;
    Collection<GameData> getAllGameData() throws DataAccessException;
    Map<Integer, GameData> getGameList() throws DataAccessException;
}