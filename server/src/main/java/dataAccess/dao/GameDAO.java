package dataAccess.dao;

import dataAccess.data.GameData;

public interface GameDAO {
    void addGame(GameData gameData);
    GameData getGame(int gameID);


    // Other game-related methods
}




//package dataAccess.dao;
//
//import dataAccess.DataAccessException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class GameDAO {
//
//    private final String jdbcUrl = "jdbc:mysql://your-database-host:3306/your-database-name";
//    private final String username = "your-username";
//    private final String password = "your-password";
//
//    // Adjust the SQL statements based on your database schema
//    private final String clearChessDataQuery = "DELETE FROM chess_table";
//
//    public void clearChessData() throws DataAccessException {
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
//             PreparedStatement preparedStatement = connection.prepareStatement(clearChessDataQuery)) {
//
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error clearing chess data");
//        }
//    }
//}
