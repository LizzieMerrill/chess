package dataAccess.dao;

import chess.ChessGame;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
    private final String username = "root";
    private final String password = "JavaRulez2!";
    Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

    private final DatabaseManager manager = new DatabaseManager();
    private final String addGameQuery = "INSERT INTO game_table(gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
    private final String getGameQuery = "SELECT * FROM game_table WHERE gameID = ?";
    private final String clearGameDataQuery = "DELETE FROM game_table";
    public SQLGameDAO() throws DataAccessException, SQLException {
        dbCreationCheck(jdbcUrl, username, password);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");

        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getGameQuery)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clearChessData() throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(clearGameDataQuery)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createGame(String gameData) throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        try (Connection connection = getConnection(gameData)) {//NOT RIGHT???
            String query = "INSERT INTO games (game_data) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, gameData);

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

//    @Override
//    public void updateGame(GameData gameData) throws DataAccessException {
//        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
//        String sql = "UPDATE games SET game_data = ?, current_player_username = ? WHERE game_id = ?";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, gameData.getGameData());
//            preparedStatement.setString(2, Integer.toString(gameData.getGameID()));
//
//            //execute the update query
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void updateGame(GameData gameData, String username) throws DataAccessException {
//        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
//        String sql = "UPDATE games SET game_data = ?, current_player_username = ? WHERE game_id = ?";
//
//        try (Connection connection = getConnection("jdbc:mysql://localhost:3306/chess"); // Open a new connection
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setString(1, gameData.getGameData());
//            preparedStatement.setString(2, username); // Assuming this is the correct method to get the current player's username
//            preparedStatement.setInt(3, gameData.getGameID());
//
//            // Execute the update query
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DataAccessException("Error updating game: " + e.getMessage());
//        }
//    }

    @Override
    public void updateGame(GameData gameData, String username) throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");

        try (Connection connection = getConnection("jdbc:mysql://localhost:3306/chess");
             PreparedStatement insertPlayerStatement = connection.prepareStatement("INSERT INTO players (game_id, username) VALUES (?, ?)");
             PreparedStatement updateGameStatement = connection.prepareStatement("UPDATE games SET game_data = ?, current_player_username = ? WHERE game_id = ?")) {

            connection.setAutoCommit(false); // Start a transaction

            try {
                // Insert a new player into the 'players' table
                insertPlayerStatement.setInt(1, gameData.getGameID());
                insertPlayerStatement.setString(2, username);
                insertPlayerStatement.executeUpdate();

                // Update the 'games' table
                updateGameStatement.setString(1, gameData.getGameData());
                updateGameStatement.setString(2, username);
                updateGameStatement.setInt(3, gameData.getGameID());
                updateGameStatement.executeUpdate();

                connection.commit(); // Commit the transaction

            } catch (SQLException e) {
                connection.rollback(); // Rollback the transaction if an error occurs
                throw new DataAccessException("Error updating game: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true); // Set back to auto-commit mode
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error updating game: " + e.getMessage());
        }
    }




    @Override
    public Collection<GameData> getAllGameData() throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        List<GameData> games = new ArrayList<>();

        try (Connection connection = getConnection(jdbcUrl, username, password)) {
            String query = "SELECT * FROM games";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving games from the database", e);
        }
        JsonArray jsonArray = new JsonArray();
        for (GameData gameData : games) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("gameID", gameData.getGameID());
            jsonObject.addProperty("gameData", gameData.getGameData().toString()); // Adjust based on your actual game type
            jsonArray.add(jsonObject);
        }

        //return the JSON array as a string, or an empty JSON object if the array is empty
        return null;
    }

    @Override
    public Map<Integer, GameData> getGameList() throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLGameDAO that = (SQLGameDAO) o;
        return Objects.equals(jdbcUrl, that.jdbcUrl) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(connection, that.connection) && Objects.equals(addGameQuery, that.addGameQuery) && Objects.equals(getGameQuery, that.getGameQuery) && Objects.equals(clearGameDataQuery, that.clearGameDataQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jdbcUrl, username, password, connection, addGameQuery, getGameQuery, clearGameDataQuery);
    }
}
