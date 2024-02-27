package dataAccess.dao;

import dataAccess.data.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    // In-memory storage for game data
    private final Map<Integer, GameData> gameDataMap;

    public MemoryGameDAO() {
        this.gameDataMap = new HashMap<>();
    }

    @Override
    public void addGame(GameData gameData) {
        gameDataMap.put(gameData.getGameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    public void clearChessData() {
        gameDataMap.clear();
    }

}
