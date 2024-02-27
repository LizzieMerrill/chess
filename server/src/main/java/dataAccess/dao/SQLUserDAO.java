package dataAccess.dao;

import dataAccess.data.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    private final String jdbcUrl = "jdbc:mysql://your-database-host:3306/your-database-name";
    private final String username = "your-username";
    private final String password = "your-password";

    // Adjust the SQL statements based on your database schema
    private final String addUserQuery = "INSERT INTO user_table(username, password, email) VALUES (?, ?, ?)";
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";

    public SQLUserDAO() {
        // Initialize your database connection if needed
        // This can include loading the JDBC driver, etc.
        // Example: Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @Override
    public void addUser(UserData userData) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery)) {
            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, userData.getPassword());
            preparedStatement.setString(3, userData.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, this.username, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(authenticateUserQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Return true if a matching user is found
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false;
        }
    }

    // Add implementations for other user-related methods if needed
    // ...
}
