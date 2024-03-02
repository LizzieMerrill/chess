package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.GameData;
import java.util.Collection;
import java.util.Map;


public interface GameDAO {
    GameData getGame(int gameID);
    void clearChessData();
    int createGame(String gameName) throws DataAccessException;
    void updateGame(GameData gameData);
    Collection<GameData> getAllGameData();
    Map<Integer, GameData> getGameList();
}