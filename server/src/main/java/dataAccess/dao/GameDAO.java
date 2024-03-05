package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;


public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    boolean clearChessData() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException, SQLException;
    Collection<GameData> getAllGameData() throws DataAccessException;
    Map<Integer, GameData> getGameList() throws DataAccessException;
}