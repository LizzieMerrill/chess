package dataAccess.data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GameData {
    private int gameID;
    private String gameData;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private Object game; // Change 'Object' to the actual type of your game
    private Set<String> watcherTokens;

    // Default constructor
    // Default constructor
    public GameData() {
        whiteUsername = null;
        blackUsername = null;
        gameName = null;
        game = null;
        watcherTokens = new HashSet<>();
    }

    // Constructor with parameters
    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, Object game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
        this.watcherTokens = new HashSet<>();
    }

    public GameData(String gameData){
        this.gameData = gameData;
    }

    // Getters and setters

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Object getGame() {
        return game;
    }

    public void setGame(Object game) {
        this.game = game;
    }
    public String getGameData(){
        return gameData;
    }

    public Set<String> getWatcherTokens() {
        return watcherTokens;
    }

    public void addWatcherToken(String watcherToken) {
        watcherTokens.add(watcherToken);
    }

    public String getCurrentPlayerAuthToken(String user) {
        if (user.equals(whiteUsername)) {
            return whiteUsername;
        } else if (user.equals(blackUsername)) {
            return blackUsername;
        } else {
            // The specified user is not the current player
            return null;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData1 = (GameData) o;
        return gameID == gameData1.gameID && Objects.equals(gameData, gameData1.gameData) && Objects.equals(whiteUsername, gameData1.whiteUsername) && Objects.equals(blackUsername, gameData1.blackUsername) && Objects.equals(gameName, gameData1.gameName) && Objects.equals(game, gameData1.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameData, whiteUsername, blackUsername, gameName, game);
    }
}
