package dataAccess.dao;

import dataAccess.data.GameData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLGameDAO implements GameDAO {
    private final String jdbcUrl = "jdbc:mysql://your-database-host:3306/your-database-name";
    private final String username = "your-username";
    private final String password = "your-password";

    // Adjust the SQL statement based on your database schema
    private final String addGameQuery = "INSERT INTO game_table(gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
    private final String getGameQuery = "SELECT * FROM game_table WHERE gameID = ?";
    private final String clearGameDataQuery = "DELETE FROM game_table"; // Adjust based on your database schema

    // Adjust the SQL statements based on your database schema

    public SQLGameDAO() {
        // Initialize your database connection if needed
        // This can include loading the JDBC driver, etc.
        // Example: Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @Override
    public void addGame(GameData gameData) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addGameQuery)) {
            preparedStatement.setInt(1, gameData.getGameID());
            preparedStatement.setString(2, gameData.getWhiteUsername());
            preparedStatement.setString(3, gameData.getBlackUsername());
            preparedStatement.setString(4, gameData.getGameName());
            preparedStatement.setObject(5, gameData.getGame()); // Adjust based on your actual game type
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public GameData getGame(int gameID) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getGameQuery)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new GameData(
                            resultSet.getInt("gameID"),
                            resultSet.getString("whiteUsername"),
                            resultSet.getString("blackUsername"),
                            resultSet.getString("gameName"),
                            resultSet.getObject("game") // Adjust based on your actual game type
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null;
    }

    @Override
    public void clearChessData() {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(clearGameDataQuery)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
