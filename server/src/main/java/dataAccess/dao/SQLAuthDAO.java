package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.AuthData;
import dataAccess.DatabaseManager;
import java.sql.*;
import java.util.Map;
import java.util.Objects;

public class SQLAuthDAO implements AuthDAO {
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
    private final String username = "root";
    private final String password = "JavaRulez2!";
    private final DatabaseManager manager = new DatabaseManager();
    boolean databaseExists = checkDatabaseExists(jdbcUrl, username, password);
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";
    private final String addAuthTokenQuery = "INSERT INTO auth_table(authToken, username) VALUES (?, ?)";
    private final String getAuthTokenQuery = "SELECT * FROM auth_table WHERE authToken = ?";
    private final String getByAuthTokenQuery = "SELECT * FROM auth_table WHERE authToken = ?";
    private final String getByUsernameQuery = "SELECT * FROM auth_table WHERE username = ?";
    private final String addAuthDataQuery = "INSERT INTO auth_table(authToken, username) VALUES (?, ?)";
    private final String removeAuthDataQuery = "DELETE FROM auth_table WHERE authToken = ?";

    public SQLAuthDAO() {
        //manager.createDatabase();
    }
    @Override
    public Map<String, AuthData> getAuthList() throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        return null;
    }

    @Override
    public void clearAuthData() throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
    }


    @Override
    public void addAuthToken(AuthData authData) throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addAuthTokenQuery)) {
            preparedStatement.setString(1, authData.getAuthToken());
            preparedStatement.setString(2, authData.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        return fetchDataByQuery(getAuthTokenQuery, authToken);
    }

    @Override
    public void removeAuthData(String authToken) throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(removeAuthDataQuery)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AuthData fetchDataByQuery(String query, String parameter) throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new AuthData(resultSet.getString("authToken"), resultSet.getString("username"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValidAuthToken(String authToken) throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connection;//obtain database connection

                    statement = connection.createStatement();

            String query = "SELECT * FROM auth_data WHERE auth_token = '" + authToken + "'";
            resultSet = statement.executeQuery(query);

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) ((Statement) statement).close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    static boolean checkDatabaseExists(String databaseUrl, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(databaseUrl, username, password);
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
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
