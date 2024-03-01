package dataAccess.dao;

import chess.ChessGame;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataAccess.data.GameData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> gameDataMap;
    //private final Map<Integer, Set<String>> watchers; // Map gameID to set of watcher tokens
    private int nextGameId = 1;
//    private final List<GameData> listOfGames;

    public MemoryGameDAO() {
        this.gameDataMap = new HashMap<>();
        //this.watchers = new ConcurrentHashMap<>();
        //this.listOfGames = new ArrayList<>();
    }

    @Override
    public void addGame(GameData gameData) {
        int gameId = gameData.getGameID();
        gameDataMap.put(gameId, gameData);
        //watchers.put(gameId, new HashSet<>());
        //listOfGames.add(gameData); // Add the game to the list
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public void clearChessData() {
        gameDataMap.clear();
        //watchers.clear();
    }

    @Override
    public int createGame(String gameName) {
        GameData newGameData = new GameData(gameName);
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
    public void updateGame(GameData gameData) {
        int gameId = gameData.getGameID();

        // Update the game data in the in-memory map
        gameDataMap.put(gameId, gameData);

        // Also update the game data in the list
//        int index = gameId;//listOfGames.indexOf(gameData);
//        if (index != -1 || index != 0) {
//            gameDataMap.put(index, gameData);
//        } else {
//            gameDataMap.(gameData); // Add the game to the list if not present
//        }

        // Add the watcher to the game's watcher set
        //gameData.setWatcherUsername(currentPlayerUsername);
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

//    @Override
//    public boolean isPlayerInGame(String authToken, int gameId) {
//        // Assuming a player is considered in the game if they are a watcher
//        return getWatcherTokens(gameId).contains(authToken);
//    }

    //@Override
    //public Set<String> getWatcherTokens(int gameId) {
//        return watchers.getOrDefault((gameId), Collections.emptySet());
//    }


//    @Override
//    public int getSpectatorCount(int gameId) {
//        if (getGame(gameId) == null) {
//            return -1; // or throw an exception, depending on your design
//        }
//
//        int index = gameId - 1;
//        if (index >= 0 && index < watchers.size()) {
//            Set<String> spectatorSet = watchers.get(index);
//            return spectatorSet.size();
//        }
//
//        return -1; // Return -1 if gameId is out of bounds
//    }
//
//    @Override
//    public boolean isWhiteTaken(int gameId) {
//        return getGame(gameId).getWhiteUsername() != null;
//    }
//
//    @Override
//    public boolean isBlackTaken(int gameId) {
//        return getGame(gameId).getBlackUsername() != null;
//    }


}
