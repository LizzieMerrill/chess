package dataAccess.dao;

import com.google.gson.JsonArray;
import dataAccess.access.DataAccessException;
import dataAccess.data.GameData;

import java.util.List;
import java.util.Set;

public interface GameDAO {
    void addGame(GameData gameData);
    GameData getGame(int gameID);
    void clearChessData();
    int createGame(String gameData) throws DataAccessException;
    void updateGame(GameData gameData, String currentPlayersUsername);
    JsonArray getAllGames();
    boolean isPlayerInGame(String authToken, String gameId);
    Set<String> getWatcherTokens(String gameId);
}