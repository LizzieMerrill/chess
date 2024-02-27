package dataAccess.dao;

import dataAccess.data.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    private final String jdbcUrl = "jdbc:mysql://your-database-host:3306/your-database-name";
    private final String username = "your-username";
    private final String password = "your-password";

    // Adjust the SQL statements based on your database schema
    private final String addAuthTokenQuery = "INSERT INTO auth_table(authToken, username) VALUES (?, ?)";
    private final String getAuthTokenQuery = "SELECT * FROM auth_table WHERE authToken = ?";
    private final String getByAuthTokenQuery = "SELECT * FROM auth_table WHERE authToken = ?";
    private final String getByUsernameQuery = "SELECT * FROM auth_table WHERE username = ?";
    private final String addAuthDataQuery = "INSERT INTO auth_table(authToken, username) VALUES (?, ?)";
    private final String removeAuthDataQuery = "DELETE FROM auth_table WHERE authToken = ?";

    public SQLAuthDAO() {
        // Initialize your database connection if needed
        // This can include loading the JDBC driver, etc.
        // Example: Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @Override
    public void addAuthToken(AuthData authData) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addAuthTokenQuery)) {
            preparedStatement.setString(1, authData.getAuthToken());
            preparedStatement.setString(2, authData.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return fetchDataByQuery(getAuthTokenQuery, authToken);
    }

    @Override
    public AuthData getByAuthToken(String authToken) {
        return fetchDataByQuery(getByAuthTokenQuery, authToken);
    }

    @Override
    public AuthData getByUsername(String username) {
        return fetchDataByQuery(getByUsernameQuery, username);
    }

    @Override
    public void addAuthData(AuthData authData) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addAuthDataQuery)) {
            preparedStatement.setString(1, authData.getAuthToken());
            preparedStatement.setString(2, authData.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public void removeAuthData(String authToken) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(removeAuthDataQuery)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private AuthData fetchDataByQuery(String query, String parameter) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new AuthData(resultSet.getString("authToken"), resultSet.getString("username"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null;
    }
}
