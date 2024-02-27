package dataAccess.dao;

import dataAccess.data.AuthData;
import dataAccess.data.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    private final String jdbcUrl = "jdbc:mysql://your-database-host:3306/your-database-name";
    private final String username = "your-username";
    private final String password = "your-password";
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";

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


//    @Override
//    public boolean authenticateUser(String username, String password) {
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, this.username, this.password);
//             PreparedStatement preparedStatement = connection.prepareStatement(authenticateUserQuery)) {
//
//            preparedStatement.setString(1, username);
//            preparedStatement.setString(2, password);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                return resultSet.next(); // Return true if a matching user is found
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle the exception appropriately
//            return false; // Authentication fails on exception
//        }
//    }



    @Override
    public boolean authenticateUser(UserData userData) {
        try (Connection connection = null;// Obtain a database connection here
                     PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?")) {

            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, userData.getPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If a row is returned, authentication is successful
            }
        } catch (SQLException e) {
            // Handle SQL exceptions, log them, or throw a custom exception
            e.printStackTrace(); // Handle this more appropriately in a real application
            return false; // Authentication failed due to an exception
        }
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
