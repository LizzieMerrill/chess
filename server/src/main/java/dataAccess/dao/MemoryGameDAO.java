package dataAccess.dao;

import model.GameData;
import java.util.*;

public class MemoryGameDAO implements GameDAO {

    public Map<Integer, GameData> gameDataMap;
    public int nextGameId = 1;

    public MemoryGameDAO() {
        this.gameDataMap = new HashMap<>();
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public boolean clearChessData() {
        gameDataMap.clear();
        nextGameId = 1;
        return false;
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
    public boolean updateGame(GameData gameData) {
//        int gameId = gameData.getGameID();
//        gameDataMap.put(gameId, gameData);
        return false;
    }



    @Override
    public Collection<GameData> getAllGameData() {
        return gameDataMap.values();
    }

    @Override
    public Map<Integer, GameData> getGameList() {
        return gameDataMap;
    }

}
