package dataAccess.dao;

import dataAccess.data.GameData;
import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> gameDataMap;
    private int nextGameId = 1;

    public MemoryGameDAO() {
        this.gameDataMap = new HashMap<>();
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public void clearChessData() {
        gameDataMap.clear();
    }

    @Override
    public int createGame(String gameName) {
        GameData newGameData = new GameData(gameName);
        int gameId = nextGameId++;
        newGameData.setGameID(gameId);

        gameDataMap.put(gameId, newGameData);
        return gameId;
    }

    @Override
    public void updateGame(GameData gameData) {
//        int gameId = gameData.getGameID();
//        gameDataMap.put(gameId, gameData);
    }



    @Override
    public Collection<GameData> getAllGames() {
        return gameDataMap.values();
    }
}
