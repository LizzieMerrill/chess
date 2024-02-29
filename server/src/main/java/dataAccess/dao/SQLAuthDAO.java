package dataAccess.dao;

import dataAccess.data.AuthData;
import dataAccess.data.UserData;

import java.sql.*;
import java.util.Objects;

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
    public void clearAuthData() {
        //TODO
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

//    @Override
//    public void addAuthData(AuthData authData) {
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
//             PreparedStatement preparedStatement = connection.prepareStatement(addAuthDataQuery)) {
//            preparedStatement.setString(1, authData.getAuthToken());
//            preparedStatement.setString(2, authData.getUsername());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle the exception appropriately
//        }
//    }

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

    public boolean isValidAuthToken(String authToken) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connection;// obtain your database connection here

                    statement = connection.createStatement();

            // Assuming you have a table named 'auth_data' with a column 'auth_token'
            String query = "SELECT * FROM auth_data WHERE auth_token = '" + authToken + "'";
            resultSet = statement.executeQuery(query);

            return resultSet.next(); // Returns true if a row is found, indicating a valid auth token

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close resources in reverse order of their creation
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) ((Statement) statement).close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLAuthDAO that = (SQLAuthDAO) o;
        return Objects.equals(jdbcUrl, that.jdbcUrl) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(authenticateUserQuery, that.authenticateUserQuery) && Objects.equals(addAuthTokenQuery, that.addAuthTokenQuery) && Objects.equals(getAuthTokenQuery, that.getAuthTokenQuery) && Objects.equals(getByAuthTokenQuery, that.getByAuthTokenQuery) && Objects.equals(getByUsernameQuery, that.getByUsernameQuery) && Objects.equals(addAuthDataQuery, that.addAuthDataQuery) && Objects.equals(removeAuthDataQuery, that.removeAuthDataQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jdbcUrl, username, password, authenticateUserQuery, addAuthTokenQuery, getAuthTokenQuery, getByAuthTokenQuery, getByUsernameQuery, addAuthDataQuery, removeAuthDataQuery);
    }
}
