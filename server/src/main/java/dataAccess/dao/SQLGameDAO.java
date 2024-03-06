package dataAccess.dao;

import dataAccess.DatabaseManager;
import dataAccess.access.DataAccessException;
import model.GameData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;

import static dataAccess.dao.SQLAuthDAO.dbCreationCheck;
import static java.sql.DriverManager.getConnection;

import java.sql.ResultSet;


public class SQLGameDAO implements GameDAO {
//    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
//    private final String username = "root";
//    private final String password = "JavaRulez2!";
private static final DatabaseManager manager = new DatabaseManager();

    private final String addGameQuery = "INSERT INTO game_table(white_username, black_username, game_name, game_id) VALUES (?, ?, ?, ?)";
    private final String getGameQuery = "SELECT * FROM game_table WHERE game_id = ?";
    private final String clearGameDataQuery = "DELETE FROM game_table";

    public int nextGameId = 1;
    public SQLGameDAO() {
        //dbCreationCheck();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getGameQuery)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    GameData gameData = new GameData();

                    // Assuming you have methods in GameData to set the values
                    gameData.setGameID(resultSet.getInt("game_id"));
                    gameData.setWhiteUsername(resultSet.getString("white_username"));
                    gameData.setBlackUsername(resultSet.getString("black_username"));
                    gameData.setGameName(resultSet.getString("game_name"));

                    return gameData;
                }
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean clearChessData() throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(clearGameDataQuery)) {

            preparedStatement.executeUpdate();
            nextGameId = 1;
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }


    @Override
    public int createGame(String gameName) throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection()) {
            String query = "INSERT INTO game_table (white_username, black_username, game_name) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, null);
                statement.setString(2, null);
                statement.setString(3, gameName);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DataAccessException("Creating game failed, no rows affected.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new DataAccessException("Creating game failed, no ID obtained.");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }
    }



    @Override
    public boolean updateGame(GameData gameData) throws DataAccessException, SQLException {
        dbCreationCheck();
try {
    Connection connection = manager.getConnection();
    PreparedStatement updateGameStatement = connection.prepareStatement("UPDATE game_table SET white_username = ?, black_username = ?, game_name = ? WHERE game_id = ?");

    // Update the 'games' table
    updateGameStatement.setString(1, gameData.getWhiteUsername());
    updateGameStatement.setString(2, gameData.getBlackUsername());
    updateGameStatement.setString(3, gameData.getGameName());
    updateGameStatement.setInt(4, gameData.getGameID());
    updateGameStatement.executeUpdate();
    return true;
}catch (Exception e) {
    return false;
        //throw new RuntimeException("Error retrieving games from the database", e);
    }

    }



    @Override
    public Collection<GameData> getAllGameData() throws DataAccessException {
        dbCreationCheck();
        Collection<GameData> games = new ArrayList<>();

        try (Connection connection = manager.getConnection()) {
            String query = "SELECT * FROM game_table";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        GameData gameData = new GameData();

                        gameData.setGameID(resultSet.getInt("game_id"));
                        gameData.setWhiteUsername(resultSet.getString("white_username"));
                        gameData.setBlackUsername(resultSet.getString("black_username"));
                        gameData.setGameName(resultSet.getString("game_name"));

                        games.add(gameData);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving games from the database", e);
        }
        return games;
    }


    @Override
    public Map<Integer, GameData> getGameList() throws DataAccessException {
        dbCreationCheck();
        Map<Integer, GameData> authMap = new HashMap<>();

        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getGameQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int gameId = resultSet.getInt("game_id");
                authMap.put(gameId, getGame(gameId));
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return authMap;
    }
    private void handleSQLException(SQLException e) throws DataAccessException {
        dbCreationCheck();
        //e.printStackTrace();
        throw new DataAccessException("SQL Exception: " + e.getMessage(), e);
    }
}
