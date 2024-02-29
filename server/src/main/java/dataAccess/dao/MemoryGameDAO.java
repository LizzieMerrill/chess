package dataAccess.dao;

import chess.ChessGame;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataAccess.data.GameData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> gameDataMap;
    private final Map<Integer, Set<String>> watchers; // Map gameID to set of watcher tokens
    private int nextGameId = 1;
    private final List<GameData> listOfGames;

    public MemoryGameDAO() {
        this.gameDataMap = new HashMap<>();
        this.watchers = new ConcurrentHashMap<>();
        this.listOfGames = new ArrayList<>();
    }

    @Override
    public void addGame(GameData gameData) {
        int gameId = gameData.getGameID();
        gameDataMap.put(gameId, gameData);
        watchers.put(gameId, new HashSet<>());
        listOfGames.add(gameData); // Add the game to the list
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public void clearChessData() {
        gameDataMap.clear();
        watchers.clear();
    }

    @Override
    public int createGame(String gameData) {
        GameData newGameData = new GameData(gameData);
        int gameId = nextGameId++;
        newGameData.setGameID(gameId);
        addGame(newGameData);
        return gameId;
    }

//    @Override
//    public void updateGame(GameData gameData) {
//        int gameId = gameData.getGameID();
//        gameDataMap.put(gameId, gameData);
//    }

//    @Override
//    public void updateGame(GameData gameData) {
//        int gameId = gameData.getGameID();
//
//        // Update the game data in the in-memory map
//        gameDataMap.put(gameId, gameData);
//
//        // Also update the game data in the list
//        int index = listOfGames.indexOf(gameData);
//        if (index != -1) {
//            listOfGames.set(index, gameData);
//        } else {
//            listOfGames.add(gameData); // Add the game to the list if not present
//        }
//
//        // Add the watcher to the game's watcher set
//        gameData.addWatcherToken(gameData.getCurrentPlayerAuthToken());
//    }

    @Override
    public void updateGame(GameData gameData, String currentPlayerUsername) {
        int gameId = gameData.getGameID();

        // Update the game data in the in-memory map
        gameDataMap.put(gameId, gameData);

        // Also update the game data in the list
        int index = listOfGames.indexOf(gameData);
        if (index != -1) {
            listOfGames.set(index, gameData);
        } else {
            listOfGames.add(gameData); // Add the game to the list if not present
        }

        // Add the watcher to the game's watcher set
        gameData.addWatcherToken(currentPlayerUsername);
    }



    @Override
    public Collection<GameData> getAllGames() {
//        JsonArray jsonArray = new JsonArray();
//        for (GameData gameData : gameDataMap.values()) {
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("gameID", gameData.getGameID());
//            jsonObject.addProperty("gameData", gameData.getGameData());
//            jsonArray.add(jsonObject);
//        }
        return gameDataMap.values();
    }

    @Override
    public boolean isPlayerInGame(String authToken, String gameId) {
        // Assuming a player is considered in the game if they are a watcher
        return getWatcherTokens(gameId).contains(authToken);
    }

    @Override
    public Set<String> getWatcherTokens(String gameId) {
        return watchers.getOrDefault(Integer.parseInt(gameId), Collections.emptySet());
    }

    @Override
    public boolean isGameCreated(String gameId) {
        return listOfGames.contains(gameId);
    }

    @Override
    public int getSpectatorCount(String gameId) {
        if (!isGameCreated(gameId)) {
            return -1; // or throw an exception, depending on your design
        }

        int index = Integer.parseInt(gameId) - 1;
        if (index >= 0 && index < watchers.size()) {
            Set<String> spectatorSet = watchers.get(index);
            return spectatorSet.size();
        }

        return -1; // Return -1 if gameId is out of bounds
    }



    // Other methods...

}
