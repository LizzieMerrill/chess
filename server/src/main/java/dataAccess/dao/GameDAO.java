package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.GameData;
import java.util.Collection;


public interface GameDAO {
    GameData getGame(int gameID);
    void clearChessData();
    int createGame(String gameData) throws DataAccessException;
    void updateGame(GameData gameData);
    Collection<GameData> getAllGames();
}