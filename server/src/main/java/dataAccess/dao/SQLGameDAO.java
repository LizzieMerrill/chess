package dataAccess.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataAccess.access.DataAccessException;
import dataAccess.data.GameData;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;
import static java.sql.DriverManager.getConnection;
import java.sql.ResultSet;


public class SQLGameDAO implements GameDAO {
    private final String jdbcUrl = "jdbc:mysql://your-database-host:3306/your-database-name";
    private final String username = "your-username";
    private final String password = "your-password";
    private Connection connection = null;
    private DataSource dataSource = null;
    private final String addGameQuery = "INSERT INTO game_table(gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
    private final String getGameQuery = "SELECT * FROM game_table WHERE gameID = ?";
    private final String clearGameDataQuery = "DELETE FROM game_table"; // Adjust based on your database schema
    public SQLGameDAO(){
        this.connection = null;
    }

    @Override
    public GameData getGame(int gameID) {
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getGameQuery)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //TODO
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clearChessData() {
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(clearGameDataQuery)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createGame(String gameData) throws DataAccessException {
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

    @Override
    public void updateGame(GameData gameData) {
        String sql = "UPDATE games SET game_data = ?, current_player_username = ? WHERE game_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameData.getGameData());
            preparedStatement.setString(3, Integer.toString(gameData.getGameID()));

            //execute the update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Collection<GameData> getAllGames() {
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
        return null;//TODO
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
