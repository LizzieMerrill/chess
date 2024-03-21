//package model;
//
//import chess.ChessGame;
//import java.util.Objects;
//
//public class GameData {
//    public int gameID;
//    private String gameData;
//    private String whiteUsername;
//    private String blackUsername;
//    private String gameName;
//    private ChessGame game; // Change 'Object' to the actual type of your game
//    public GameData(String gameName){
//        this.gameName = gameName;
//    }
//    public GameData(){
//
//    }
//
//    // Getters and setters
//
//    public int getGameID() {
//        return gameID;
//    }
//
//    public void setGameID(int gameID) {
//        this.gameID = gameID;
//    }
//
//    public String getWhiteUsername() {
//        return whiteUsername;
//    }
//
//    public void setWhiteUsername(String whiteUsername) {
//        this.whiteUsername = whiteUsername;
//    }
//
//    public String getBlackUsername() {
//        return blackUsername;
//    }
//
//    public void setBlackUsername(String blackUsername) {
//        this.blackUsername = blackUsername;
//    }
//    public String getGameData(){
//        return gameData;
//    }
//    public void setGameName(String gameName){
//        this.gameName = gameName;
//    }
//    public String getGameName(){
//        return gameName;
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        GameData gameData1 = (GameData) o;
//        return gameID == gameData1.gameID && Objects.equals(gameData, gameData1.gameData) && Objects.equals(whiteUsername, gameData1.whiteUsername) && Objects.equals(blackUsername, gameData1.blackUsername) && Objects.equals(gameName, gameData1.gameName) && Objects.equals(game, gameData1.game);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(gameID, gameData, whiteUsername, blackUsername, gameName, game);
//    }
//}


package model;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameData {
    public int gameID;
    private String gameData;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;
    private GameStatus status;
    private List<String> observers;

    public GameData(String gameName){
        this.gameName = gameName;
        this.observers = new ArrayList<>();
    }

    public GameData(){
        this.observers = new ArrayList<>();
    }

    // Getters and setters

    public void addObserver(String username) {
        observers.add(username);
    }

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

    public String getGameData(){
        return gameData;
    }

    public void setGameName(String gameName){
        this.gameName = gameName;
    }

    public String getGameName(){
        return gameName;
    }

    // Add getStatus method
    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData1 = (GameData) o;
        return gameID == gameData1.gameID && Objects.equals(gameData, gameData1.gameData) && Objects.equals(whiteUsername, gameData1.whiteUsername) && Objects.equals(blackUsername, gameData1.blackUsername) && Objects.equals(gameName, gameData1.gameName) && Objects.equals(game, gameData1.game) && status == gameData1.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameData, whiteUsername, blackUsername, gameName, game, status);
    }
}
