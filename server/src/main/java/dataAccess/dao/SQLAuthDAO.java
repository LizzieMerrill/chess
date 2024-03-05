package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.AuthData;
import dataAccess.DatabaseManager;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.sql.DriverManager.getConnection;

public class SQLAuthDAO implements AuthDAO {
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
    private final String username = "root";
    private final String password = "JavaRulez2!";
    private static final DatabaseManager manager = new DatabaseManager();
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";
    private final String addAuthTokenQuery = "INSERT INTO auth_table(username, auth_token) VALUES (?, ?)";
    private final String getAuthTokenQuery = "SELECT * FROM auth_table WHERE auth_token = ?";
    private final String getByAuthTokenQuery = "SELECT * FROM auth_table WHERE auth_token = ?";
    private final String getByUsernameQuery = "SELECT * FROM auth_table WHERE username = ?";
    private final String addAuthDataQuery = "INSERT INTO auth_table(username, auth_token) VALUES (?, ?)";
    private final String removeAuthDataQuery = "DELETE FROM auth_table WHERE auth_token = ?";
    private final String clearAuthDataQuery = "DELETE FROM auth_table";

    public SQLAuthDAO() throws DataAccessException {
        dbCreationCheck(jdbcUrl, username, password);
    }
    @Override
    public Map<String, AuthData> getAuthList() throws DataAccessException {
        Map<String, AuthData> authMap = new HashMap<>();

        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getByAuthTokenQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String authToken = resultSet.getString("auth_token");
                String username = resultSet.getString("username");
                authMap.put(authToken, new AuthData(authToken, username));
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return authMap;
    }

    @Override
    public boolean clearAuthData() throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(clearAuthDataQuery)) {

            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean addAuthToken(AuthData authData) throws DataAccessException {
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addAuthTokenQuery)) {

            preparedStatement.setString(1, authData.getUsername());
            preparedStatement.setString(2, authData.getAuthToken());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return false;
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        return fetchDataByQuery(getAuthTokenQuery, authToken);
    }

    @Override
    public boolean removeAuthData(String authToken) throws DataAccessException {
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(removeAuthDataQuery)) {

            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return false;
    }

    private AuthData fetchDataByQuery(String query, String parameter) throws DataAccessException {
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new AuthData(resultSet.getString("auth_token"), resultSet.getString("username"));
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }
    @Override
    public boolean isValidAuthToken(String authToken) throws DataAccessException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection(jdbcUrl, username, password);

            statement = connection.createStatement();

            String query = "SELECT * FROM auth_table WHERE auth_token = '" + authToken + "'";
            resultSet = statement.executeQuery(query);

            return resultSet.next();

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                //e.printStackTrace();
            }
        }
    }


    public static void dbCreationCheck(String databaseUrl, String username, String password) throws DataAccessException {
        boolean dbExists;
        try {
            Connection connection = getConnection(databaseUrl, username, password);
            connection.close();
            dbExists = true;
        } catch (SQLException e) {
            dbExists = false;
        }

        if(dbExists){
            createTableIfNotExists("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!", "game_table");
            createTableIfNotExists("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!", "auth_table");
            createTableIfNotExists("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!", "user_table");
        }
        else{
            manager.createDatabase();

            createTableIfNotExists("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!", "game_table");
            createTableIfNotExists("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!", "auth_table");
            createTableIfNotExists("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!", "user_table");
        }
    }
    private static void createTableIfNotExists(String jdbcUrl, String username, String password, String tableName) {
        try (Connection connection = getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            if (!tableExists(statement, tableName)) {

                String createTableQuery = "CREATE TABLE " + tableName + " ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "column1 VARCHAR(255),"
                        + "column2 INT)";
                // If the table doesn't exist, create it
                if(tableName.equals("game_table")){
                    createTableQuery = "CREATE TABLE " + tableName + " ("
                            + "white_username VARCHAR(255),"
                            + "black_username VARCHAR(255),"
                            + "game_name VARCHAR(255),"
                            + "game_id INT PRIMARY KEY AUTO_INCREMENT)";
                }
                if(tableName.equals("user_table")){
                    createTableQuery = "CREATE TABLE " + tableName + " ("
                            + "username VARCHAR(255),"
                            + "password VARCHAR(255),"
                            + "email VARCHAR(255))";
                }
                if(tableName.equals("auth_table")){
                    createTableQuery = "CREATE TABLE " + tableName + " ("
                            + "username VARCHAR(255),"
                            + "auth_token VARCHAR(255))";
                }
                statement.executeUpdate(createTableQuery);
            }

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    private static boolean tableExists(Statement statement, String tableName) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
        return resultSet.next();
    }

    private void handleSQLException(SQLException e) throws DataAccessException {
        //e.printStackTrace();
        throw new DataAccessException("SQL Exception: " + e.getMessage(), e);
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
