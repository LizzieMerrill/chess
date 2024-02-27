package dataAccess.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dataAccess.data.GameData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryGameDAO implements GameDAO {

    // In-memory storage for game data
    private final Map<Integer, GameData> gameDataMap;
    private int nextGameId = 1;
    private final Map<String, GameData> games = new ConcurrentHashMap<>();
    private List<GameData> listOfGames;

    public MemoryGameDAO() {
        this.gameDataMap = new HashMap<>();
        this.listOfGames = new ArrayList<>();
    }

    @Override
    public void addGame(GameData gameData) {
        gameDataMap.put(gameData.getGameID(), gameData);
        listOfGames.add(gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public void clearChessData() {
        gameDataMap.clear();
        listOfGames.clear();
    }

    @Override
    public int createGame(String gameData) {
        // Placeholder implementation, replace with actual logic
        GameData newGameData = new GameData(gameData);
        addGame(newGameData);
        return newGameData.getGameID();
    }

    @Override
    public void updateGame(GameData gameData) {
        // Assuming each game is identified by a unique key, such as game ID
        String gameId = Integer.toString(gameData.getGameID());

        // Update the game data in the in-memory map
        games.put(gameId, gameData);

        // Also update the game data in the list
        int index = listOfGames.indexOf(gameData);
        if (index != -1) {
            listOfGames.set(index, gameData);
        }
    }

    @Override
    public JsonArray getAllGames() {
        try {
            // Fetch the list of games from listOfGames
            List<GameData> gamesList = new ArrayList<>(listOfGames);

            // Convert the list to a JSON array
            JsonArray jsonArray = new JsonArray();
            for (GameData gameData : gamesList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("gameID", gameData.getGameID());
                jsonObject.addProperty("gameData", gameData.getGameData());
                jsonArray.add(jsonObject);
            }

            // Return the JSON array
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving games from memory: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryGameDAO that = (MemoryGameDAO) o;
        return nextGameId == that.nextGameId && Objects.equals(gameDataMap, that.gameDataMap) && Objects.equals(games, that.games) && Objects.equals(listOfGames, that.listOfGames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameDataMap, nextGameId, games, listOfGames);
    }
}
