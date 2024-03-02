package dataAccess.dao;

import model.AuthData;

import java.sql.*;
import java.util.Map;
import java.util.Objects;

public class SQLAuthDAO implements AuthDAO {
    private final String jdbcUrl = "jdbc:mysql://your-database-host:3306/your-database-name";
    private final String username = "your-username";
    private final String password = "your-password";
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";
    private final String addAuthTokenQuery = "INSERT INTO auth_table(authToken, username) VALUES (?, ?)";
    private final String getAuthTokenQuery = "SELECT * FROM auth_table WHERE authToken = ?";
    private final String getByAuthTokenQuery = "SELECT * FROM auth_table WHERE authToken = ?";
    private final String getByUsernameQuery = "SELECT * FROM auth_table WHERE username = ?";
    private final String addAuthDataQuery = "INSERT INTO auth_table(authToken, username) VALUES (?, ?)";
    private final String removeAuthDataQuery = "DELETE FROM auth_table WHERE authToken = ?";

    public SQLAuthDAO() {
        //initialize Class.forName("com.mysql.cj.jdbc.Driver");
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
            e.printStackTrace();
        }
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return fetchDataByQuery(getAuthTokenQuery, authToken);
    }

    @Override
    public void removeAuthData(String authToken) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(removeAuthDataQuery)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValidAuthToken(String authToken) {
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

    @Override
    public Map<String, AuthData> getAuthList() {
        return null;
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
